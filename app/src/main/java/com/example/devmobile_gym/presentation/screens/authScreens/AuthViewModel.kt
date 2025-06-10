package com.example.devmobile_gym.presentation.screens.authScreens

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.devmobile_gym.data.repository.UserWorkoutsRepository
import com.example.devmobile_gym.domain.model.Usuario.Aluno
import com.example.devmobile_gym.domain.model.Professor
import com.example.devmobile_gym.domain.model.UserWorkouts
import com.example.devmobile_gym.domain.model.Usuario
import com.example.devmobile_gym.domain.repository.UserWorkoutsRepositoryModel
import com.example.devmobile_gym.presentation.screens.authScreens.register.RegisterViewModel
import com.example.devmobile_gym.utils.isProfessorEmail
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    val auth : FirebaseAuth = FirebaseAuth.getInstance()
    private val db : FirebaseFirestore = FirebaseFirestore.getInstance()
    private val streakRepository : UserWorkoutsRepositoryModel = UserWorkoutsRepository()

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
    private fun createUserInstance(user: FirebaseUser, nome: String, peso: Double = 0.0, altura: Double = 0.0): Usuario {

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
                peso = peso,
                altura = altura,
                email = user.email
            )
        }


    }

    fun isOnlyNumbers(input: String): Boolean {
        return input.matches(Regex("^\\d+(\\.\\d+)?$"))
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

    var isLoginComplete by mutableStateOf(false)
    // metodos de autenticacao
    fun login(email : String, senha : String) {
        isLoginComplete = false // ← Resetar estado
        if (email.isEmpty() || senha.isEmpty()) {
            _authState.value = AuthState.Error("Email ou senha não podem estar vazios.")
            return
        }

        _authState.value = AuthState.Loading
        auth.signInWithEmailAndPassword(email, senha).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                _authState.value = AuthState.Authenticated
                isLoginComplete = true
            } else {
                _authState.value = AuthState.Error("Email ou senha incorretos")
            }
        }
    }

    var isRegistrationComplete by mutableStateOf(false)

    fun signup(email: String, nome: String, senha: String, confirmarSenha: String, peso: String, altura: String) {
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
            !isOnlyNumbers(peso) && !isOnlyNumbers(altura) -> {
                _authState.value = AuthState.Error("Peso e altura inválidos.")
                return
            }
            !isOnlyNumbers(peso) -> {
                _authState.value = AuthState.Error("Peso inválido.")
                return
            }
            !isOnlyNumbers(altura) -> {
                _authState.value = AuthState.Error("Altura inválida.")
                return
            }
            else -> {
                _authState.value = AuthState.Loading
                auth.createUserWithEmailAndPassword(email, senha)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val user = auth.currentUser ?: run {
                                _authState.value = AuthState.Error("Usuário não criado")
                                return@addOnCompleteListener
                            }

                            // Lançar uma coroutine para criar o streak e salvar o usuário
                            viewModelScope.launch {
                                try {
                                    streakRepository.criarStreak(userId = user.uid)
                                    Log.d("SignupViewModel", "Streak inicializado com sucesso ")

                                    val pesoDouble = peso.toDouble()
                                    val alturaDouble = altura.toDouble()
                                    val usuario = createUserInstance(user, nome, pesoDouble, alturaDouble)
                                    when (usuario){
                                        is Usuario.Aluno -> saveToFirestore(usuario, "alunos")
                                        is Usuario.Professor -> saveToFirestore(usuario, "professores")
                                    }
                                    Log.d("SignupViewModel", "Usuário criado com sucesso ")
                                } catch (e: Exception) {
                                    Log.e("SignupViewModel", "Erro ao criar streak ou salvar usuário: ", e)
                                    // Se a criação do streak ou o salvamento do usuário falhar
                                    // definir o estado de erro aqui.
                                    _authState.value = AuthState.Error("Erro ao finalizar o cadastro. Tente novamente.")
                                    auth.currentUser?.delete()
                                }
                            }
                        } else {
                            val exception = task.exception
                            if (exception is FirebaseAuthUserCollisionException) {
                                // Se a exceção for do tipo FirebaseAuthUserCollisionException,
                                // significa que o e-mail já está em uso.
                                _authState.value = AuthState.Error("Este e-mail já está cadastrado. Por favor, use outro.")
                            } else {
                                _authState.value = AuthState.Error(task.exception?.message ?: "Erro no cadastro")
                            }
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
