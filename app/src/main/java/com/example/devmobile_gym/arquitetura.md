Com base na estrutura atual do seu projeto e visando aplicar a arquitetura MVVM + Clean Architecture, a reorganização ideal seria:

```
com.example.devmobile_gym
|
├── data
│   ├── remote              # Fontes de dados remotas (APIs, Retrofit)
│   ├── local               # Fontes de dados locais (Room, SharedPrefs)
│   ├── dto                 # Data Transfer Objects usados na rede
│   ├── repository          # Implementações concretas dos repositórios
│   └── mock                # Dados mockados para simulação do app sem backend
│       └── MockData.kt     # Lista de usuários, professores, rotinas, treinos e exercícios simulados
|
├── domain
│   ├── model               # Modelos puros usados no core da aplicação
│   │   ├── Pessoa.kt
│   │   ├── Professor.kt
│   │   ├── Rotina.kt
│   │   ├── Treino.kt
│   │   └── Exercicio.kt
│   ├── usecase             # Casos de uso (regras de negócio)
│   └── repository          # Interfaces dos repositórios usados na domain
|
├── presentation
│   ├── components          # Componentes reutilizáveis com Compose
│   ├── navigation          # Controle de rotas com NavHost
│   ├── screens             # Telas separadas por funcionalidade
│   │   ├── login
│   │   │   ├── LoginScreen.kt
│   │   │   ├── LoginViewModel.kt
│   │   │   └── LoginState.kt
│   │   ├── home
│   │   │   ├── HomeScreen.kt
│   │   │   └── HomeViewModel.kt
│   │   ├── register
│   │   │   ├── RegisterScreen.kt
│   │   │   └── RegisterViewModel.kt
│   │   └── dashboard_professor
│   │       ├── DashboardProfessorScreen.kt
│   │       └── DashboardProfessorViewModel.kt
│   └── MainActivity.kt     # Entry point do app
|
├── di
│   └── AppModule.kt        # Configuração do Hilt (injeção de dependência)
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

