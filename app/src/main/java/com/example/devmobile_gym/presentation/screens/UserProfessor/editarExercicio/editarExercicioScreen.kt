// presentation/screens/UserProfessor/editarExercicio/EditarExercicioScreen.kt
package com.example.devmobile_gym.presentation.screens.UserProfessor.editarExercicio

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.components.ui.theme.components.CustomButton
import com.example.devmobile_gym.R
import com.example.devmobile_gym.presentation.components.CustomScreenScaffoldProfessor
import com.example.devmobile_gym.presentation.components.CustomTextField
import com.example.devmobile_gym.presentation.navigation.AuthRoutes
import com.example.devmobile_gym.presentation.navigation.ProfessorRoutes
import com.example.devmobile_gym.presentation.screens.UserProfessor.adicionaMaquinaExercicio.editarExercicioViewModel
import com.example.devmobile_gym.presentation.screens.UserAluno.profile.ProfilePictureState
import com.example.devmobile_gym.presentation.screens.authScreens.AuthState
import com.example.devmobile_gym.presentation.screens.authScreens.AuthViewModel

@Composable
fun EditarExercicioScreen(
    navController: NavHostController,
    onBack: () -> Unit,
    onConclude: () -> Unit,
    backStackEntry: NavBackStackEntry
) {
    // Instanciar o ViewModel usando a Factory
    val viewModel: editarExercicioViewModel = viewModel(
        viewModelStoreOwner = backStackEntry,
        factory = editarExercicioViewModel.Factory
    )

    // Coleta o objeto de exercício carregado, se necessário
    val exercicio by viewModel.exercicioSelecionado
    val exercicioPhotoBitmap by viewModel.exercicioPhotoBitmap.collectAsState()
    val exercicioPhotoState by viewModel.exercicioPhotoState.collectAsState()

    val context = LocalContext.current

    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            viewModel.uploadExercicioPhoto(context, uri)
        }
    }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val selectedItemIndex = when (currentRoute) {
        ProfessorRoutes.Home -> 0
        ProfessorRoutes.Aulas -> 1
        ProfessorRoutes.AdicionarRotina -> 2
        ProfessorRoutes.Chatbot -> 3
        ProfessorRoutes.Gerenciar -> 4
        else -> 0
    }

    val authViewModel: AuthViewModel = viewModel()
    val authState by authViewModel.authState.observeAsState()

    LaunchedEffect(authState) {
        if (authState == AuthState.Unauthenticated) {
            navController.navigate(AuthRoutes.Login) {
                popUpTo(0)
                launchSingleTop = true
            }
        }
    }

    val saveSuccess by viewModel.saveSuccess

    LaunchedEffect(saveSuccess) {
        if (saveSuccess) {
            onConclude()
        }
    }

    CustomScreenScaffoldProfessor(
        needToGoBack = true,
        onBackClick = { onBack() },
        navController = navController,
        selectedItemIndex = selectedItemIndex
    ) { innerModifier ->
        val combinedModifier = innerModifier.padding(1.dp)

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = combinedModifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .background(Color(0xFF1E1E1E))
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            // --- UI para a Foto do Exercício ---
            Box(
                modifier = Modifier
                    .size(180.dp)
                    .clip(CircleShape)
                    .background(Color.Gray)
                    .clickable { pickImageLauncher.launch("image/*") },
                contentAlignment = Alignment.Center
            ) {
                if (exercicioPhotoBitmap != null) {
                    Image(
                        bitmap = exercicioPhotoBitmap!!.asImageBitmap(),
                        contentDescription = "Foto do exercício",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    // Placeholder se não houver foto (ou ainda não foi carregada)
                    Icon(
                        imageVector = Icons.Default.Info, // Ícone para adicionar foto
                        contentDescription = "Placeholder para foto do exercício",
                        tint = Color.White,
                        modifier = Modifier.size(80.dp)
                    )
                }

                if (exercicioPhotoState is ProfilePictureState.Loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(60.dp),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            if (exercicioPhotoState is ProfilePictureState.Error) {
                Text(
                    text = (exercicioPhotoState as ProfilePictureState.Error).message,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
            // --- Fim UI para a Foto do Exercício ---

            Spacer(modifier = Modifier.height(32.dp))

            CustomTextField(
                label = "Nome do exercicio",
                value = viewModel.novoNome.value,
                onValueChange = viewModel::onNovoNomeChange,
                padding = 10,
                modifier = Modifier
            )

            CustomTextField(
                label = "Grupo Muscular",
                value = viewModel.novoGrupoMuscular.value,
                onValueChange = viewModel::onNovoGrupoMuscularChange,
                padding = 10,
                modifier = Modifier
            )

            CustomTextField(
                label = "Descrição do Exercício",
                value = viewModel.novaDescricao.value,
                onValueChange = viewModel::onNovaDescricaoChange,
                padding = 10,
                modifier = Modifier
            )

            Spacer(modifier = Modifier.height(32.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                CustomButton(
                    text = "Concluir Edição",
                    onClick = {
                        exercicio?.let { // Garante que há um exercício selecionado
                            viewModel.editarExercicio(
                                newName = viewModel.novoNome.value,
                                newGrupoMuscular = viewModel.novoGrupoMuscular.value,
                                newDescricao = viewModel.novaDescricao.value
                            )
                        }
                    }
                )
            }
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}