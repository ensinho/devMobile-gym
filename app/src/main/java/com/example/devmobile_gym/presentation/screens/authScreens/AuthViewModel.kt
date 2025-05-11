package com.example.devmobile_gym.presentation.screens.authScreens

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

class AuthViewModel : ViewModel() {

    private val auth : FirebaseAuth = FirebaseAuth.getInstance()

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

    // No AuthViewModel
    fun signup(email: String, senha: String, confirmarSenha: String) {
        when {
            senha.isEmpty() -> {
                _authState.value = AuthState.Error("Preencha todos os campos")
                return
            }
            senha != confirmarSenha -> {
                _authState.value = AuthState.Error("As senhas não coincidem")
                return
            }
            senha.length < 6 -> {
                _authState.value = AuthState.Error("Senha deve ter pelo menos 6 caracteres")
                return
            }
            else -> {
                _authState.value = AuthState.Loading
                auth.createUserWithEmailAndPassword(email, senha)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            _authState.value = AuthState.Authenticated
                        } else {
                            _authState.value = AuthState.Error(task.exception?.message ?: "Erro desconhecido")
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
