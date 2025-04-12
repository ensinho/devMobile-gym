
---

## 🧠 O que é o ViewModel?

O `ViewModel`:
- Representa a **lógica de apresentação** da sua tela (UI).
- Guarda os dados que a UI precisa exibir.
- Sobrevive a mudanças de configuração (ex: rotação da tela).
- É o elo entre a **UI** (tela) e a **lógica de negócio** (casos de uso/repos).
- Idealmente, ele **não conhece a UI diretamente**, só fornece os dados que a UI consome.

---

## 🧩 O que é o State?

O `State`:
- Representa **o estado atual da tela**, como os valores dos campos, se há erro, se está carregando etc.
- É uma **classe de dados (data class)** imutável que pode ser observada pela UI.
- Sempre que o `State` é alterado, a tela (UI) é **recomposta automaticamente** para refletir essas mudanças.

---

## 🔁 Como eles interagem?

1. O `ViewModel` tem um `State` (geralmente um `MutableState` ou `StateFlow`).
2. A UI observa esse `State`.
3. Quando o usuário interage com a tela (por exemplo, digitando ou clicando), um evento chama uma **função do ViewModel**.
4. O ViewModel **atualiza o State**.
5. A UI **reage automaticamente** à mudança.

---

## 📌 Exemplo prático

### ✅ Estado da tela (State.kt)

```kotlin
data class LoginState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
```

---

### ✅ ViewModel da tela (LoginViewModel.kt)

```kotlin
class LoginViewModel : ViewModel() {

    var state by mutableStateOf(LoginState())
        private set

    fun onEmailChanged(newEmail: String) {
        state = state.copy(email = newEmail)
    }

    fun onPasswordChanged(newPassword: String) {
        state = state.copy(password = newPassword)
    }

    fun onLoginClick() {
        viewModelScope.launch {
            state = state.copy(isLoading = true, errorMessage = null)

            // Simula um login (esperaria o resultado de um use case ou repositório)
            delay(2000)

            if (state.email == "admin" && state.password == "1234") {
                state = state.copy(isLoading = false)
                // navegação pode ser acionada pela UI com base nesse estado
            } else {
                state = state.copy(isLoading = false, errorMessage = "Credenciais inválidas")
            }
        }
    }
}
```

---

### ✅ Tela que observa o ViewModel (LoginScreen.kt)

```kotlin
@Composable
fun LoginScreen(viewModel: LoginViewModel = viewModel()) {
    val state = viewModel.state

    Column(modifier = Modifier.padding(16.dp)) {

        if (state.errorMessage != null) {
            Text(
                text = state.errorMessage,
                color = Color.Red,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        OutlinedTextField(
            value = state.email,
            onValueChange = viewModel::onEmailChanged,
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = state.password,
            onValueChange = viewModel::onPasswordChanged,
            label = { Text("Senha") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { viewModel.onLoginClick() },
            modifier = Modifier.fillMaxWidth(),
            enabled = !state.isLoading
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(16.dp))
            } else {
                Text("Entrar")
            }
        }
    }
}
```

---

## 🔁 Ciclo da Interação

1. Usuário digita o e-mail → `viewModel.onEmailChanged()` → atualiza o estado.
2. Usuário clica em "Entrar" → `viewModel.onLoginClick()` → muda `isLoading`, faz verificação → atualiza `errorMessage` se der erro.
3. UI recompõe automaticamente com base no `state`.

---

## 💡 Dicas finais

- Sempre que possível, trate o `State` como **imutável**.
- Você pode usar `StateFlow` ou `MutableState` para controle fino.
- Mantenha **apenas o ViewModel responsável por modificar o estado**.

---

Se quiser, posso gerar um esqueleto base para esse padrão ou aplicar ao seu app atual, com base na estrutura que você montou. Só dizer 😄