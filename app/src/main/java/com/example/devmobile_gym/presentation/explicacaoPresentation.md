### 📁 `presentation/`
Responsável por tudo que o usuário vê. Usa Jetpack Compose para mostrar os dados vindos do `ViewModel`.

- **`components/`**  
  Componentes de UI reutilizáveis (botões personalizados, cards, campos de texto, etc).

- **`navigation/`**  
  Onde está seu `NavHost`, definindo as rotas de navegação da aplicação.

- **`screens/`**  
  Onde ficam suas telas. Ideal separar por funcionalidades. Cada funcionalidade geralmente contém:

    - `Screen.kt` → Interface do usuário
    - `ViewModel.kt` → Conecta os dados e ações do `usecase` com a UI
    - `State.kt` → Estado da tela (campos, loading, erro etc.)

- **`MainActivity.kt`**  
  O ponto de entrada do app. Aqui você configura o tema e o `NavHost`.

---