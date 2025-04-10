package com.example.devmobile_gym.navigation

import RegisterScreen
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.devmobile_gym.ui.theme.screens.LoginScreen
import com.example.devmobile_gym.ui.theme.screens.RegisterScreen2


@Composable
fun AppNavHost() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(onNavigateToRegister = {
                navController.navigate("register")
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

    }
}
