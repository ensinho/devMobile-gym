package com.example.devmobile_gym.presentation.components

import android.graphics.Color
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.devmobile_gym.R
import com.example.devmobile_gym.presentation.navigation.AlunoRoutes
import com.example.devmobile_gym.presentation.navigation.AuthRoutes
import com.example.devmobile_gym.presentation.screens.authScreens.AuthViewModel
import com.example.devmobile_gym.ui.theme.White

@Composable
fun DrawerContent(
    navController: NavHostController,
    closeMenu: () -> Unit // Adicionando a função de fechar o menu
) {
    val authViewModel: AuthViewModel = viewModel()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Row (
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ){
            Text(
                text = "Menu",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(vertical = 16.dp),
                color = White
            )

            // Botão de fechar o menu (ícone X ou similar)
            IconButton(onClick = closeMenu) {
                Icon(
                    imageVector = Icons.Default.Close, // Ícone de fechar
                    contentDescription = "Fechar Menu",
                    modifier = Modifier.size(24.dp)
                )
            }
        }


        Divider()

        Spacer(modifier = Modifier.height(16.dp))

        DrawerItem(title = "Página Inicial", iconResId = R.drawable.home_icon, onClick = {
            navController.navigate(
                AlunoRoutes.Home
            )
        })
        DrawerItem(title = "Perfil", iconResId = R.drawable.profile_icon, onClick = {
            navController.navigate(
                AlunoRoutes.Profile
            )
        })
        DrawerItem(title = "Buscar", iconResId = R.drawable.search_icon, onClick = {
            navController.navigate(
                AlunoRoutes.Search
            )
        })
        DrawerItem(title = "Aulas", iconResId = R.drawable.class_icon, onClick = {
            navController.navigate(
                AlunoRoutes.Aulas
            )
        })
        DrawerItem(title = "ChatBot", iconResId = R.drawable.chat_icon_filled, onClick = {
            navController.navigate(
                AlunoRoutes.Chatbot
            )
        })
        DrawerItem(
            title = "Sair",
            iconResId = R.drawable.baseline_logout_24,
            onClick = {
//                authViewModel.signout()
                navController.navigate(AuthRoutes.Login) {
                    popUpTo(0) // Limpa toda a pilha de navegação
                    launchSingleTop = true
                }
                closeMenu()
            }
        )
    }
}


@Composable
private fun DrawerItem(
    title: String,
    iconResId: Int,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.Start
    ) {
        Icon(
            painter = painterResource(id = iconResId),
            contentDescription = title,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}
