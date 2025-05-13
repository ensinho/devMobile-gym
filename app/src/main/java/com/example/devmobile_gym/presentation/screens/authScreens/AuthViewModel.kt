package com.example.devmobile_gym.presentation.screens.authScreens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.devmobile_gym.domain.model.Aluno
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class AuthViewModel : ViewModel() {

    private val auth : FirebaseAuth = FirebaseAuth.getInstance()
    private val db : FirebaseFirestore = FirebaseFirestore.getInstance()

    private val _authState = MutableLiveData<AuthState>()
    val authState : MutableLiveData<AuthState> = _authState

    init {
        checkAuthStatus()
    }

    fun checkAuthStatus() {
        if (auth.currentUser == null) {
            _authState.value = AuthState.Unauthenticated
        } else {
            _authState.value = AuthState.Authenticated
        }
    }

    // No AuthViewModel.kt
    fun resetAuthState() {
        _authState.value = AuthState.Unauthenticated
    }

    fun login(email : String, senha : String) {
        if (email.isEmpty() || senha.isEmpty()) {
            _authState.value = AuthState.Error("Email ou senha não podem estar vazios.")
            return
        }

        _authState.value = AuthState.Loading
        auth.signInWithEmailAndPassword(email, senha).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                _authState.value = AuthState.Authenticated
            } else {
                _authState.value = AuthState.Error(task.exception?.message ?: "Erro desconhecido")
            }
        }
    }

    var isRegistrationComplete by mutableStateOf(false)

    // No AuthViewModel
    fun signup(email: String, nome: String, senha: String, confirmarSenha: String) {
        isRegistrationComplete = false // ← Resetar estado

        when {
            senha.isEmpty() -> {
                _authState.value = AuthState.Error("Preencha todos os campos.")
                return
            }
            senha != confirmarSenha -> {
                _authState.value = AuthState.Error("As senhas não coincidem.")
                return
            }
            senha.length < 6 -> {
                _authState.value = AuthState.Error("Senha deve ter pelo menos 6 caracteres.")
                return
            }
            else -> {
                _authState.value = AuthState.Loading
                auth.createUserWithEmailAndPassword(email, senha)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // Garanta que o usuário foi criado
                            val user = auth.currentUser ?: run {
                                _authState.value = AuthState.Error("Usuário não criado")
                                return@addOnCompleteListener
                            }
                            val userData = Aluno(
                                uid = user.uid,
                                nome = nome,
                                email = user.email
                            )
                            userData.uid?.let {
                                db
                                    .collection("Aluno")
                                    .document(it)
                                    .set(userData)
                                    .addOnSuccessListener {
                                        isRegistrationComplete = true
                                        _authState.value = AuthState.Authenticated
                                    }
                                    .addOnFailureListener { e ->
                                        _authState.value = AuthState.Error(e.message ?: "Erro ao salvar dados: ${e.message}")
                                        auth.currentUser?.delete() // Opcional: remove usuário criado se falhar no Firestore
                                    }
                            }
                        } else {
                            _authState.value = AuthState.Error(task.exception?.message ?: "Erro no cadastro")
                        }
                    }
            }
        }
    }

    fun signout() {
        auth.signOut()
        _authState.value = AuthState.Unauthenticated
    }
}
