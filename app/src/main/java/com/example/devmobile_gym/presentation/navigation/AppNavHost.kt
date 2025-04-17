package com.example.devmobile_gym.navigation

import com.example.devmobile_gym.presentation.screens.register.RegisterScreen
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.devmobile_gym.presentation.screens.concluiTreino.ConcluiTreino
import com.example.devmobile_gym.presentation.screens.detalhesTreino.DetalhesTreinoScreen
import com.example.devmobile_gym.presentation.screens.home.HomeScreen
import com.example.devmobile_gym.presentation.screens.login.LoginScreen
import com.example.devmobile_gym.presentation.screens.register.RegisterScreen2


@Composable
fun AppNavHost() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(
                onNavigateToRegister = {
                navController.navigate("register")
            },
                onNavigateToHome = {
                navController.navigate("Home")
            })
        }

        composable("register") {
            RegisterScreen(onNavigateToRegister2 = {
                navController.navigate("register2")
            })
        }

        composable("register2") {
            RegisterScreen2(onBack = {
                navController.popBackStack()
            })
        }

        composable("Home") {
            HomeScreen(onNavigateToTreino = {
                navController.navigate("detalhesTreino/$it")
            })
        }

        composable(
            route = "detalhesTreino/{treinoId}",
            arguments = listOf(
                navArgument("treinoId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            DetalhesTreinoScreen(backStackEntry = backStackEntry, onBack = {
                navController.popBackStack()
            })
        }

        composable(
            route = "concluirTreino/{treinoSelecionadoId}",
            arguments = listOf(
                navArgument("treinoSelecionadoId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            ConcluiTreino(
                backStackEntry = backStackEntry,
                onBack = {
                navController.popBackStack()
                },
                onConclude = {
                    navController.navigate("Home")
                }
            )
        }

        // Adicionar as novas telas

    }
}
