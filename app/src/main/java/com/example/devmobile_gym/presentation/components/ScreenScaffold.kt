// Arquivo: MainScaffold.kt
package com.example.devmobile_gym.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.devmobile_gym.R
import com.example.devmobile_gym.presentation.navigation.AlunoRoutes
import kotlinx.coroutines.launch

// classe que representa cada item da navbar
data class BottomNavigationItem(
    val title: String,
    val selectedIcon: NavIcon,
    val unselectedIcon: NavIcon,
    val hasNews: Boolean,
    val badgeCount: Int? = null
)

// sealed class para suportar ícones diferentes
sealed class NavIcon {
    data class VectorIcon(val icon: androidx.compose.ui.graphics.vector.ImageVector) : NavIcon()
    data class DrawableIcon(val resId: Int) : NavIcon()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomScreenScaffold(
    navController: NavHostController,
    needToGoBack: Boolean = false,
    onBackClick: () -> Unit,
    selectedItemIndex: Int,
    content: @Composable (Modifier) -> Unit,
    color: Color = Color(0xFF1E1E1E),
    onMenuClick: () -> Unit
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerContent = {
            Surface(color = Color(0xFF1E1E1E)) {
                DrawerContent(
                    navController = navController,
                    closeMenu = {
                        scope.launch { drawerState.close() }
                    },
                )
            }
        },
        drawerState = drawerState,
        gesturesEnabled = true,
    ) {
        val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

        val itemsUser = listOf(
            BottomNavigationItem("Home", NavIcon.DrawableIcon(R.drawable.home_icon), NavIcon.DrawableIcon(R.drawable.home_icon), false),
            BottomNavigationItem("Search", NavIcon.VectorIcon(Icons.Filled.Search), NavIcon.VectorIcon(Icons.Outlined.Search), false),
            BottomNavigationItem("QR Code", NavIcon.DrawableIcon(R.drawable.qr_code_icon), NavIcon.DrawableIcon(R.drawable.qr_code_icon), false),
            BottomNavigationItem("ChatBot", NavIcon.DrawableIcon(R.drawable.chat_icon_filled), NavIcon.DrawableIcon(R.drawable.chat_icon_outlined), false),
            BottomNavigationItem("Profile", NavIcon.VectorIcon(Icons.Filled.Person), NavIcon.VectorIcon(Icons.Outlined.Person), false)
        )

        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {
                if (needToGoBack) {
                    CenterAlignedTopAppBar(
                        title = { TitleScaffold() },
                        navigationIcon = {
                            IconButton(onClick = { onBackClick() }) {
                                Icon(Icons.Default.KeyboardArrowLeft, contentDescription = "Voltar")
                            }
                        },
                        actions = {
                            IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                Icon(Icons.Default.Menu, contentDescription = "Menu")
                            }
                        },
                        scrollBehavior = scrollBehavior
                    )
                } else {
                    TopAppBar(
                        title = { TitleScaffold() },
                        actions = {
                            IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                Icon(Icons.Default.Menu, contentDescription = "Menu")
                            }
                        },
                        scrollBehavior = scrollBehavior,
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = color, // azul escuro
                            titleContentColor = Color.White,
                            actionIconContentColor = Color.White
                        )
                    )
                }
            },
            bottomBar = {
                NavigationBar(
                    containerColor = Color(0xFF2B2B2B),
                    tonalElevation = 6.dp,
                    modifier = Modifier.clip(RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp))
                ) {
                    itemsUser.forEachIndexed { index, item ->
                        if (index == 2) {
                            IconButton(
                                onClick = { navController.navigate(AlunoRoutes.QrCode) },
                                modifier = Modifier
                                    .padding(top = 0.dp)
                                    .background(Color.White, shape = CircleShape)
                                    .padding(3.dp)
                                    .background(Color(0xFF1E88E5), shape = CircleShape)
                                    .padding(6.dp)
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.qr_code_icon),
                                    contentDescription = item.title,
                                    tint = Color.White,
                                    modifier = Modifier.height(24.dp)
                                )
                            }
                        } else {
                            NavigationBarItem(
                                selected = selectedItemIndex == index,
                                onClick = {
                                    when (index) {
                                        0 -> navController.navigate(AlunoRoutes.Home) {
                                            popUpTo(AlunoRoutes.Home) { inclusive = true }
                                            launchSingleTop = true
                                        }
                                        1 -> navController.navigate(AlunoRoutes.Search) {
                                            popUpTo(AlunoRoutes.Search) { inclusive = true }
                                            launchSingleTop = true
                                        }
                                        3 -> navController.navigate(AlunoRoutes.Chatbot) {
                                            popUpTo(AlunoRoutes.Chatbot) { inclusive = true }
                                            launchSingleTop = true
                                        }
                                        4 -> navController.navigate(AlunoRoutes.Profile) {
                                            popUpTo(AlunoRoutes.Profile) { inclusive = true }
                                            launchSingleTop = true
                                        }
                                    }
                                },
                                label = { Text(item.title, color = Color.White) },
                                alwaysShowLabel = true,
                                icon = {
                                    BadgedBox(
                                        badge = {
                                            when {
                                                item.badgeCount != null -> {
                                                    Badge { Text(text = item.badgeCount.toString()) }
                                                }
                                                item.hasNews -> {
                                                    Badge()
                                                }
                                            }
                                        }
                                    ) {
                                        val iconToShow = if (index == selectedItemIndex) item.selectedIcon else item.unselectedIcon
                                        when (iconToShow) {
                                            is NavIcon.VectorIcon -> {
                                                Icon(imageVector = iconToShow.icon, contentDescription = item.title)
                                            }
                                            is NavIcon.DrawableIcon -> {
                                                Icon(painter = painterResource(id = iconToShow.resId), contentDescription = item.title)
                                            }
                                            else -> {}
                                        }
                                    }
                                }
                            )
                        }
                    }
                }
            }
        ) { innerPadding ->
            content(Modifier.padding(innerPadding))
        }
    }
}
