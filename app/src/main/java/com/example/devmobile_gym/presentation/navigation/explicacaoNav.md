
---

## 🚀 O que é o `NavController`?

O `NavController` é um **componente de navegação** do Jetpack Navigation Compose que:
- Controla qual **tela está visível** no momento.
- Gerencia a **pilha de navegação** (ex: `push`, `pop` de rotas).
- Permite **passar argumentos entre telas**.
- É o equivalente ao `NavController` tradicional no XML, mas 100% em Kotlin com Compose.

---

## 🔁 Como ele funciona?

- Você define um **NavHost** com **rotas (screens)**.
- O `NavController` **navega entre essas rotas** com `navigate("rota")`.
- Cada tela associada a uma rota é um Composable.

---

## 🧱 Estrutura básica com o `NavController`

### 1. Definir as rotas (strings ou sealed class)

```kotlin
sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Home : Screen("home")
    object Details : Screen("details/{id}") {
        fun createRoute(id: Int) = "details/$id"
    }
}
```

---

### 2. Criar o `NavHost`

```kotlin
@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        composable(Screen.Login.route) {
            LoginScreen(onNavigate = { navController.navigate(it) })
        }
        composable(Screen.Home.route) {
            HomeScreen(onNavigate = { navController.navigate(it) })
        }
        composable("details/{id}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")
            DetailScreen(id = id)
        }
    }
}
```

---

### 3. Iniciar o `NavController` na MainActivity

```kotlin
setContent {
    val navController = rememberNavController()
    DevMobilegymTheme {
        AppNavHost(navController = navController)
    }
}
```

---

### 4. Usar dentro da tela para navegar

```kotlin
@Composable
fun LoginScreen(onNavigate: (String) -> Unit) {
    Column {
        Button(onClick = { onNavigate(Screen.Home.route) }) {
            Text("Entrar")
        }
    }
}
```

---

## ✅ Por que não usar diretamente o `NavController` no ViewModel?

- O `ViewModel` **não conhece** a UI, então **ele não deve chamar `navigate()` diretamente.**
- Em vez disso, **a tela observa o ViewModel** e **navega com base no estado**, ou você passa uma `callback` de navegação (como o `onNavigate` no exemplo).

---

## 🧠 Resumo prático:

| Conceito                  | Função                                                        |
|--------------------------|---------------------------------------------------------------|
| `NavController`          | Controla para onde a navegação vai                            |
| `NavHost`                | Define quais telas (rotas) o app pode mostrar                 |
| `composable("rota")`     | Define o que mostrar em cada rota                             |
| `navigate("rota")`       | Comando para mudar de tela                                    |
| `rememberNavController()`| Cria e guarda o `NavController` dentro de um Composable       |

---

Perfeito! Vamos fazer isso passo a passo. Vou adaptar um `AppNavHost` com base em um exemplo realista com telas de `Login`, `Home` e `Details`, usando `sealed class` para rotas e passando argumentos de forma segura.

---

## 🧱 1. Definindo as rotas com `sealed class`

Crie um arquivo, por exemplo: `Screen.kt` em `presentation.navigation`

```kotlin
package com.example.devmobile_gym.presentation.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Home : Screen("home")
    object Details : Screen("details/{id}") {
        fun createRoute(id: Int): String = "details/$id"
    }
}
```

---

## 🌐 2. Criando o `AppNavHost`

Arquivo: `AppNavHost.kt` (em `presentation.navigation`)

```kotlin
package com.example.devmobile_gym.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.devmobile_gym.presentation.screens.login.LoginScreen
import com.example.devmobile_gym.presentation.screens.home.HomeScreen
import com.example.devmobile_gym.presentation.screens.details.DetailsScreen

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        composable(Screen.Login.route) {
            LoginScreen(onNavigate = { navController.navigate(it) })
        }
        composable(Screen.Home.route) {
            HomeScreen(onNavigate = { navController.navigate(it) })
        }
        composable(Screen.Details.route) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")?.toIntOrNull()
            DetailsScreen(id = id)
        }
    }
}
```

---

## 🧭 3. Atualizando a `MainActivity`

```kotlin
package com.example.devmobile_gym

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.navigation.compose.rememberNavController
import com.example.devmobile_gym.presentation.navigation.AppNavHost
import com.example.devmobile_gym.ui.theme.DevMobilegymTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DevMobilegymTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    val navController = rememberNavController()
                    AppNavHost(navController = navController)
                }
            }
        }
    }
}
```

---

## 🖥️ 4. Exemplo de tela com navegação

### `LoginScreen.kt`

```kotlin
@Composable
fun LoginScreen(onNavigate: (String) -> Unit) {
    Column {
        Button(onClick = { onNavigate(Screen.Home.route) }) {
            Text("Entrar")
        }
    }
}
```

### `HomeScreen.kt`

```kotlin
@Composable
fun HomeScreen(onNavigate: (String) -> Unit) {
    Column {
        Button(onClick = { onNavigate(Screen.Details.createRoute(42)) }) {
            Text("Ver detalhes do item 42")
        }
    }
}
```

### `DetailsScreen.kt`

```kotlin
@Composable
fun DetailsScreen(id: Int?) {
    Text(text = "Detalhes do item com ID: $id")
}
```

---

Se quiser, posso gerar esse esqueleto todo para o seu projeto com base nos nomes reais das suas telas (como `Login`, `Cadastro`, `Perfil`, etc). Quer?