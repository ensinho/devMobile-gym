Com base na estrutura atual do seu projeto e visando aplicar a arquitetura MVVM + Clean Architecture, a reorganização ideal seria:

```
com.example.devmobile_gym
|
├── data
│   ├── remote              # Fontes de dados remotas (APIs, Retrofit)
│   ├── local               # Fontes de dados locais (Room, SharedPrefs)
│   ├── dto                 # Data Transfer Objects usados na rede
│   └── repository          # Implementações concretas dos repositórios
|
├── domain
│   ├── model               # Modelos puros usados no core da aplicação
│   ├── usecase             # Casos de uso (regras de negócio)
│   └── repository          # Interfaces dos repositórios usados na domain
|
├── presentation
│   ├── components          # Componentes reutilizáveis com Compose
│   ├── navigation          # Controle de rotas com NavHost e definição de rotas
│   │   ├── AppNavHost.kt       # Composable central de navegação
│   │   └── Screen.kt           # Sealed class com as rotas da aplicação
│   ├── screens             # Telas separadas por funcionalidade
│   │   ├── login
│   │   │   ├── LoginScreen.kt
│   │   │   ├── LoginViewModel.kt
│   │   │   └── LoginState.kt
│   │   ├── cadastro
│   │   │   ├── CadastroScreen.kt
│   │   │   ├── CadastroViewModel.kt
│   │   │   └── CadastroState.kt
│   │   ├── home
│   │   │   ├── HomeScreen.kt
│   │   │   ├── HomeViewModel.kt
│   │   │   └── HomeState.kt
│   │   ├── perfil
│   │   │   ├── PerfilScreen.kt
│   │   │   ├── PerfilViewModel.kt
│   │   │   └── PerfilState.kt
│   └── MainActivity.kt  # Entry point do app
|
├── di
│   └── AppModule.kt     # Configuração do Hilt (injeção de dependência)
|
└── ui.theme
    ├── Color.kt
    ├── Theme.kt
    └── Type.kt
```

### Notas importantes:
- **Cada funcionalidade possui sua própria pasta em `screens/`, com tela, ViewModel e estado.**
- **As rotas são organizadas em `navigation`, com o `AppNavHost` e uma `sealed class` chamada `Screen`.**
- **Os repositórios são definidos por interfaces na `domain` e implementados na `data`.**
- **Use cases ficam na camada `domain` e representam a lógica de negócio reutilizável.**
- **O `AppNavHost` é o ponto central de navegação e fica dentro de `presentation.navigation`.**

