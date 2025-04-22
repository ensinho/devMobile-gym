package com.example.devmobile_gym.navigation

import com.example.devmobile_gym.presentation.screens.register.RegisterScreen
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.devmobile_gym.presentation.navigation.AlunoRoutes
import com.example.devmobile_gym.presentation.navigation.AuthRoutes
import com.example.devmobile_gym.presentation.navigation.ProfessorRoutes
import com.example.devmobile_gym.presentation.screens.UserAluno.AulasFucionais.ShowAulas
import com.example.devmobile_gym.presentation.screens.UserAluno.chatBot.ChatBotScreen
import com.example.devmobile_gym.presentation.screens.UserAluno.concluiTreino.ConcluiTreino
import com.example.devmobile_gym.presentation.screens.UserAluno.detalhesTreino.DetalhesTreinoScreen
import com.example.devmobile_gym.presentation.screens.UserAluno.historico.HistoricoScreen
import com.example.devmobile_gym.presentation.screens.UserAluno.home.HomeScreen

import com.example.devmobile_gym.presentation.screens.login.LoginScreen
import com.example.devmobile_gym.presentation.screens.UserAluno.profile.profileScrenn
import com.example.devmobile_gym.presentation.screens.register.RegisterScreen2
import com.example.devmobile_gym.presentation.screens.UserAluno.searchScreen.SearchScreen
import com.example.devmobile_gym.presentation.screens.UserProfessor.gerenciaAluno.GerenciaAlunoScreen
import com.example.devmobile_gym.presentation.screens.UserProfessor.home.AulasFuncionais.AulasProfessorScreen
import com.example.devmobile_gym.presentation.screens.UserProfessor.home.Home.ProfessorHomeScreen


@Composable
fun AppNavHost() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = AuthRoutes.Login) {

        // Auth Route (Login e Register)
        composable(AuthRoutes.Login) {
            LoginScreen(
                onNavigateToRegister = {
                navController.navigate(AuthRoutes.Register)
                                       },
                onNavigateToHomeAluno = {
                navController.navigate(AlunoRoutes.Home)
                                        },
                onNavigateToHomeProfessor = {
                navController.navigate(ProfessorRoutes.Home)
                }
            )
        }

        composable(AuthRoutes.Register) {
            RegisterScreen(onNavigateToRegister2 = {
                navController.navigate(AuthRoutes.Register2)
            })
        }

        composable(AuthRoutes.Register2) {
            RegisterScreen2(onBack = {
                navController.popBackStack()
            })
        }

        // TODO(vvvvvvROTAS DO ALUNOvvvvvv)

        // HOME Aluno
        composable(AlunoRoutes.Home) {
            HomeScreen(
                onNavigateToTreino = {
                navController.navigate("aluno/detalhesTreino/$it")
                },
                navController = navController,
                onNavigateToAulas = {
                    navController.navigate(AlunoRoutes.Aulas)
                }
            )
        }

        // ALUNO detalhes treino
        composable(
            route = AlunoRoutes.DetalhesTreino,
            arguments = listOf(
                navArgument("treinoId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            DetalhesTreinoScreen(
                backStackEntry = backStackEntry,
                onBack = {
                navController.popBackStack()
                },
                onConclude = { treinoId ->
                    navController.navigate("aluno/concluirTreino/$treinoId")
                },
                navController = navController

            )
        }

        // ALUNO concluir treino
        composable(
            route = AlunoRoutes.ConcluirTreino,
            arguments = listOf(
                navArgument("treinoId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            ConcluiTreino(
                backStackEntry = backStackEntry,
                onBack = {
                navController.popBackStack()
                },
                onConclude = {
                    navController.navigate(AlunoRoutes.Home)
                },
                navController = navController
            )
        }

        // ALUNO BUSCA
        composable(AlunoRoutes.Search) {
            SearchScreen(navController = navController)
        }

        // ALUNO chatbot
        composable(AlunoRoutes.Chatbot) {
            ChatBotScreen(
                onBack = {
                    navController.popBackStack()
                         },
                navController = navController)
        }

        // ALUNO HISTORICO DE TREINOS
        composable(AlunoRoutes.Historico) {
            HistoricoScreen(
                onBack = {
                    navController.popBackStack()
                         },
                navController = navController
            )
        }

        // ALUNO TELA DE AULAS
        composable(AlunoRoutes.Aulas) {
            ShowAulas(
                onBack = {
                    navController.popBackStack()
                         },
                navController = navController
            )
        }

        // ALUNO PERFIL
        composable(AlunoRoutes.Profile) {
            profileScrenn(
                navController = navController,
                onNavigateToHistorico = {
                    navController.navigate(AlunoRoutes.Historico)
                }
            )
        }
        // TODO(^^^^^^^^ ROTAS DO ALUNO ^^^^^^^^)


        // TODO(vvvvvv ROTAS DO PROFESSOR vvvvvv)
        // HOME Professor
        composable(ProfessorRoutes.Home) {
            ProfessorHomeScreen(
                navController = navController,
                onNavigateToAluno = {
                    navController.navigate("professor/detalhesAluno/$it")
                },
//                onNavigateToAulas = {
//                    navController.navigate("aulas")
//                }
            )
        }

        composable(ProfessorRoutes.Aulas){
            AulasProfessorScreen(
                navController = navController,
                onNavigateToAulas = {
                    navController.navigate(ProfessorRoutes.Aulas)
                }
            )
        }

        // HOME do ALUNO (visão Professor)
        composable(
            route = ProfessorRoutes.DetalhesAluno,
            arguments = listOf(
                navArgument("alunoId") { type = NavType.IntType }
            )

        ) { BackStackEntry ->
            GerenciaAlunoScreen(
                backStackEntry = BackStackEntry,
                navController = navController,
                onBack = {
                    navController.popBackStack()
                },
            )
        }

    }
}
