## Alterar o RegisterViewModel para verificar o domínio e definir o booleano isProfessor:

class RegisterViewModel() : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance() // Vou deixar login/registro/log-out no authViewModel

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email.asStateFlow()

    private val _senha = MutableStateFlow("")
    val senha: StateFlow<String> = _senha.asStateFlow()

    private val _confirmSenha = MutableStateFlow("")
    val confirmSenha: StateFlow<String> = _confirmSenha.asStateFlow()

    private val _nome = MutableStateFlow("")
    val nome: StateFlow<String> = _nome.asStateFlow()

    var errorMessage by mutableStateOf<String?>(null)
        private set

    var uiState by mutableStateOf<RegisterUiState>(RegisterUiState.Idle)
        private set

    fun onEmailChange(newEmail: String) {
        _email.value = newEmail
    }

    fun onNomeChange(newNome: String) {
        _nome.value = newNome
    }

    fun onSenhaChange(newSenha: String) {
        _senha.value = newSenha
    }

    fun onConfirmaSenhaChange(newConfirmSenha: String) {
        _confirmSenha.value = newConfirmSenha
    }

    // Função para verificar o domínio e retornar o booleano isProfessor
    fun verificarDominioProfessor(email: String): Boolean {
        val dominioProfessor = "professor@universidade.com" // Substitua pelo domínio desejado
        return email.contains(dominioProfessor)
    }

    // Função para validar o email
    fun validaEmail(email: String, onSuccess: (Usuario) -> Unit) {
        val emailRegex = Regex("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")
        val isValido = emailRegex.matches(email)

        if (!isValido) {
            errorMessage = "Email inválido"
        } else {
            // Verifica se o email pertence ao domínio específico e define o valor de isProfessor
            val isProfessor = verificarDominioProfessor(email)
            val usuario = Usuario(email, _nome.value, isProfessor) // Cria um usuário com isProfessor

            errorMessage = null
            onSuccess(usuario)
        }
    }

    fun validarCamposRegistro1(email: String, nome: String, onSuccess: (Usuario) -> Unit) {
        if (email.isEmpty() || nome.isEmpty()) {
            errorMessage = "Preencha todos os campos."
        } else if (nome.length < 3) { // Validação de nome
            errorMessage = "Nome deve ter pelo menos 3 caracteres"
        } else {
            validaEmail(email, onSuccess)
        }
    }
}


## Exemplo de classe 
data class Usuario(
    val email: String,
    val nome: String,
    var isProfessor: Boolean = false
)

## Exemplo de caso

val email = "usuario@professor@universidade.com"
val nome = "Professor"

validarCamposRegistro1(email, nome) { usuario ->
    println("Email: ${usuario.email}")
    println("Nome: ${usuario.nome}")
    println("É professor? ${usuario.isProfessor}")
}

