package com.example.devmobile_gym.navigation

import com.example.devmobile_gym.presentation.screens.authScreens.register.RegisterScreen
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.devmobile_gym.presentation.navigation.AlunoRoutes
import com.example.devmobile_gym.presentation.navigation.AuthRoutes
import com.example.devmobile_gym.presentation.navigation.ProfessorRoutes
import com.example.devmobile_gym.presentation.screens.UserAluno.AulasFucionais.ShowAulas
import com.example.devmobile_gym.presentation.screens.UserAluno.concluiTreino.ConcluiTreino
import com.example.devmobile_gym.presentation.screens.UserAluno.detalhesTreino.DetalhesTreinoScreen
import com.example.devmobile_gym.presentation.screens.UserAluno.historico.HistoricoScreen
import com.example.devmobile_gym.presentation.screens.UserAluno.home.HomeScreen

import com.example.devmobile_gym.presentation.screens.authScreens.login.LoginScreen
import com.example.devmobile_gym.presentation.screens.UserAluno.profile.profileScrenn
import com.example.devmobile_gym.presentation.screens.authScreens.register.RegisterScreen2
import com.example.devmobile_gym.presentation.screens.UserAluno.searchScreen.SearchScreen
import com.example.devmobile_gym.presentation.screens.UserProfessor.gerenciaAluno.GerenciaAlunoScreen
import com.example.devmobile_gym.presentation.screens.UserProfessor.AulasFuncionais.AulasProfessorScreen
import com.example.devmobile_gym.presentation.screens.UserProfessor.adicionaEditaAula.AdicionaEditaAula
import com.example.devmobile_gym.presentation.screens.UserProfessor.adicionaMaquinaExercicio.AdicionaMaquinaScreen
import com.example.devmobile_gym.presentation.screens.UserProfessor.home.ProfessorHomeScreen
import com.example.devmobile_gym.presentation.screens.UserProfessor.criarTreino.CriarTreinoScreen
import com.example.devmobile_gym.presentation.screens.UserProfessor.editarExercicio.EditarExercicioScreen
import com.example.devmobile_gym.presentation.screens.UserProfessor.editarExercicio.adicionarExercicioScreen
import com.example.devmobile_gym.presentation.screens.UserProfessor.editarTreino.EditarTreinoScreen
import com.example.devmobile_gym.presentation.screens.UserProfessor.gerenciarMaquinasExercicios.GerenciarMaquinasExerciciosScreen
import com.example.devmobile_gym.presentation.screens.authScreens.AuthViewModel
import com.example.devmobile_gym.presentation.screens.chatbotScreens.AlunoChatbotScreen
import com.example.devmobile_gym.presentation.screens.chatbotScreens.ProfessorChatbotScreen
import java.net.URLDecoder

@Composable
fun AppNavHost() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = AuthRoutes.Login) {

        // Auth Route (Login e Register)
        composable(AuthRoutes.Login) {
            LoginScreen(
                navController = navController
            )
        }

        // No AppNavHost.kt
        composable(AuthRoutes.Register) {
            val authViewModel: AuthViewModel = viewModel()
            LaunchedEffect(Unit) {
                authViewModel.resetAuthState()
                authViewModel.isRegistrationComplete = false // ← Adicione esta linha
            }
            RegisterScreen(navcontroller = navController)
        }

        composable(
            route = AuthRoutes.Register2,
            arguments = listOf(
                navArgument("email") { type = NavType.StringType },
                navArgument("nome") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            // Decodifica o email
            val email = URLDecoder.decode(
                backStackEntry.arguments?.getString("email") ?: "",
                "UTF-8"
            )
            val nome = backStackEntry.arguments?.getString("nome") ?: ""

            RegisterScreen2(
                email = email,
                nome = nome,
                navController = navController,
            )
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
                navArgument("treinoId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            DetalhesTreinoScreen(
                backStackEntry = backStackEntry,
                onBack = {
                navController.popBackStack()
                },
                navController = navController

            )
        }

        // ALUNO concluir treino
        composable(
            route = AlunoRoutes.ConcluirTreino,
            arguments = listOf(
                navArgument("treinoId") { type = NavType.StringType },
                navArgument("tempoTreino") { type = NavType.StringType }

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

//        // ALUNO chatbot
        composable(AlunoRoutes.Chatbot) {
            AlunoChatbotScreen(
                navController = navController
            )
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
                }
            )
        }

        composable(ProfessorRoutes.Aulas){
            AulasProfessorScreen(
                navController = navController
            )
        }

        // CRIAR TREINO ( visao professor )
        composable(
            route = ProfessorRoutes.AdicionarRotina + "/{uid}",
            arguments = listOf(
                navArgument("uid") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            CriarTreinoScreen(
                navController = navController,
                backStackEntry = backStackEntry,
                onBack = { navController.popBackStack() }
            )
        }

        //composable(
        //    route = "${ProfessorRoutes.CriarTreino}/{alunoId}",
        //    arguments = listOf(navArgument("alunoId") { type = NavType.StringType })
        //) { backStackEntry ->
        //    val alunoId = backStackEntry.arguments?.getString("alunoId")
        //    CriarTreinoScreen(
        //        navController = navController,
        //        backStackEntry = backStackEntry,
        //        alunoId = alunoId,
        //        onBack = { navController.popBackStack() }
        //    )
        //}

        // HOME do ALUNO (visão Professor)
        composable(
            route = ProfessorRoutes.DetalhesAluno,
            arguments = listOf(
                navArgument("uid") { type = NavType.StringType }
            )

        ) { BackStackEntry ->
            GerenciaAlunoScreen(
                backStackEntry = BackStackEntry,
                navController = navController,
                onBack = {
                    navController.popBackStack()
                },
                navigateToEdit = {
                    navController.navigate(ProfessorRoutes.EditarTreino + "/$it")
                }
            )
        }

        // PROFESSOR chatbot
        composable(ProfessorRoutes.Chatbot

        ) {
            ProfessorChatbotScreen(
                navController = navController
            )
        }

        composable(ProfessorRoutes.Gerenciar) {


            GerenciarMaquinasExerciciosScreen(
                onBack = {
                    navController.popBackStack()
                },
                navController = navController
            )
        }
        composable(ProfessorRoutes.AdicionarNovaMaquina) {
            AdicionaMaquinaScreen(
                onBack = {
                    navController.popBackStack()
                },
                navController = navController,
                onConclude = { navController.popBackStack() }
            )
        }


        composable(ProfessorRoutes.AdicionaEditaAula) {
            AdicionaEditaAula(
                navController = navController,
                onBack = {navController.popBackStack()}
            )
        }

        composable(
            route = ProfessorRoutes.EditarTreino + "/{id}",
            arguments = listOf(
                navArgument("id") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            EditarTreinoScreen(
                navController = navController,
                backStackEntry = backStackEntry,
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = ProfessorRoutes.EditarExercicio + "/{MaqExerc.id}", // Rota com parâmetro
            arguments = listOf(
                navArgument("MaqExerc.id") {
                    type = NavType.StringType // Tipo do parâmetro
                }
            )
        ) {backStackEntry ->
            EditarExercicioScreen(
                backStackEntry = backStackEntry,
                onBack = {
                    navController.popBackStack()
                },
                navController = navController,
                onConclude = { navController.navigate(ProfessorRoutes.Gerenciar) }
            )
        }

        composable(ProfessorRoutes.AdicionarExercicio) {
            adicionarExercicioScreen(
                onBack = {
                    navController.popBackStack()
                },
                navController = navController,
                onConclude = { navController.navigate(ProfessorRoutes.Gerenciar) }
            )
        }

    }
}
