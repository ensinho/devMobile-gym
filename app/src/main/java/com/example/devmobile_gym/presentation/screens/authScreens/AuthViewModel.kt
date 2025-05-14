package com.example.devmobile_gym.presentation.screens.authScreens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.devmobile_gym.domain.model.Aluno
import com.example.devmobile_gym.domain.model.Professor
import com.example.devmobile_gym.domain.model.Usuario
import com.example.devmobile_gym.presentation.screens.authScreens.register.RegisterViewModel
import com.example.devmobile_gym.utils.isProfessorEmail
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

class AuthViewModel : ViewModel() {

    val auth : FirebaseAuth = FirebaseAuth.getInstance()
    private val db : FirebaseFirestore = FirebaseFirestore.getInstance()

    private val _authState = MutableLiveData<AuthState>()
    val authState : MutableLiveData<AuthState> = _authState

    init {
        checkAuthStatus()
    }

    // metodos auxiliares
    val currentUserEmail: String? get() = auth.currentUser?.email

    fun isCurrentUserProfessor(): Boolean = currentUserEmail?.isProfessorEmail() == true

    fun checkAuthStatus() {
        if (auth.currentUser == null) {
            _authState.value = AuthState.Unauthenticated
        } else {
            _authState.value = AuthState.Authenticated
        }
    }

    // retorna o usuario instanciado no tipo correto baseado no email
    private fun createUserInstance(user: FirebaseUser, nome: String): Usuario {

        // metodo para separar os usuários em suas respectivas tabelas.
        return if (isCurrentUserProfessor()) {
            Usuario.Professor(
                uid = user.uid,
                nome = nome,
                email = user.email
            )
        } else {
            // aluno possui o campo rotina que inicialmente (quando cria a conta) é nulo.
            Usuario.Aluno(
                uid = user.uid,
                nome = nome,
                email = user.email
            )
        }


    }

    private fun saveToFirestore(usuario: Usuario, colecao: String) {
        usuario.uid?.let { uid ->
            db.collection(colecao)
                .document(uid)
                .set(usuario)
                .addOnSuccessListener {
                    _authState.value = AuthState.Authenticated
                    isRegistrationComplete = true
                }
                .addOnFailureListener { e ->
                    _authState.value = AuthState.Error(e.message ?: "Erro ao salvar dados: ${e.message}")
                    auth.currentUser?.delete() // Opcional: remove usuário criado se falhar no Firestore
                }
        }
    }

    fun resetAuthState() {
        _authState.value = AuthState.Unauthenticated
    }

    // metodos de autenticacao
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
                            val usuario = createUserInstance(user, nome)

                            when (usuario){
                                is Usuario.Aluno -> saveToFirestore(usuario, "alunos")
                                is Usuario.Professor -> saveToFirestore(usuario, "professores")
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
