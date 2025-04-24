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
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.devmobile_gym.R
import com.example.devmobile_gym.presentation.navigation.ProfessorRoutes
import kotlinx.coroutines.launch

// classe que representa cada item da navbar
data class BottomNavigationItemProfessor(
    val title: String,
    // o NavIcon se adapta de acordo com o valor passado para os icons (VectorIcon ou DrawableIcon)
    val selectedIcon: NavIcon,
    val unselectedIcon: NavIcon,
    val hasNews: Boolean,
    val badgeCount: Int? = null
)

// sealed class que suporta as variações de tipo do selectedIcon e do unselectedIcon
/*LEMBRETE o ":" significa herança.
* VectorIcon e DrawableIcon herdam NavIcon.
*/
sealed class NavIconProfessor {
    data class VectorIcon(val icon: ImageVector) : NavIcon()
    data class DrawableIcon(val resId: Int) : NavIcon()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomScreenScaffoldProfessor(
    navController: NavHostController,
    needToGoBack: Boolean = false,
    onBackClick: () -> Unit,
    selectedItemIndex: Int,
    content: @Composable (Modifier) -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    // Estado do Drawer
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val itemsUser = listOf(
        BottomNavigationItemProfessor("Home", NavIconProfessor.DrawableIcon(R.drawable.home_icon), NavIconProfessor.DrawableIcon(R.drawable.home_icon), false),
        BottomNavigationItemProfessor("Aulas", NavIconProfessor.DrawableIcon(R.drawable.ic_treinos), NavIconProfessor.DrawableIcon(R.drawable.ic_treinos), false),
        BottomNavigationItemProfessor("Adicionar Rotina", NavIconProfessor.DrawableIcon(R.drawable.ic_centroprofessor), NavIconProfessor.DrawableIcon(R.drawable.ic_centroprofessor), false),
        BottomNavigationItemProfessor("ChatBot", NavIconProfessor.DrawableIcon(R.drawable.chat_icon_filled), NavIconProfessor.DrawableIcon(R.drawable.chat_icon_outlined), false),
        BottomNavigationItemProfessor("Gerenciar", NavIconProfessor.DrawableIcon(R.drawable.ic_chave_fenda), NavIconProfessor.DrawableIcon(R.drawable.ic_chave_fenda), false)
    )

    // ENVOLVE o Scaffold com ModalNavigationDrawer
    ModalNavigationDrawer(
        drawerContent = {
            Surface(color = Color(0xFF1E1E1E)) {
                ProfessorDrawerContent (
                    navController = navController,
                    closeMenu = { scope.launch { drawerState.close() } }
                )
            }
        },
        drawerState = drawerState,
        gesturesEnabled = true,
    ) {
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
                                Icon(Icons.Default.KeyboardArrowLeft, contentDescription = "Voltar para a tela anterior.")
                            }
                        },
                        actions = {
                            IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                Icon(Icons.Default.Menu, contentDescription = "Abrir menu lateral.")
                            }
                        },
                        scrollBehavior = scrollBehavior
                    )
                } else {
                    TopAppBar(
                        title = { TitleScaffold() },
                        actions = {
                            IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                Icon(Icons.Default.Menu, contentDescription = "Abrir menu lateral.")
                            }
                        },
                        scrollBehavior = scrollBehavior
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
                                onClick = { navController.navigate(ProfessorRoutes.AdicionarRotina) },
                                modifier = Modifier
                                    .padding(top = 0.dp)
                                    .background(Color.White, shape = CircleShape)
                                    .padding(3.dp)
                                    .background(Color(0xFF1E88E5), shape = CircleShape)
                                    .padding(6.dp)
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_centroprofessor),
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
                                        0 -> navController.navigate(ProfessorRoutes.Home)
                                        1 -> navController.navigate(ProfessorRoutes.Aulas)
                                        3 -> navController.navigate(ProfessorRoutes.Chatbot)
                                        4 -> navController.navigate(ProfessorRoutes.Gerenciar)
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
                                        val iconToShow =
                                            if (index == selectedItemIndex) item.selectedIcon else item.unselectedIcon
                                        when (iconToShow) {
                                            is NavIconProfessor.VectorIcon -> {
                                                Icon(iconToShow.icon, contentDescription = item.title)
                                            }
                                            is NavIconProfessor.DrawableIcon -> {
                                                Icon(
                                                    painter = painterResource(id = iconToShow.resId),
                                                    contentDescription = item.title
                                                )
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
