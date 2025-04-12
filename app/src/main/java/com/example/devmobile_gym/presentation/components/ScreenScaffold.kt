package com.example.devmobile_gym.presentation.components

import android.graphics.drawable.Drawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import com.example.devmobile_gym.R

// classe que representa cada item da navbar
data class BottomNavigationItem(
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
sealed class NavIcon {
    data class VectorIcon(val icon: ImageVector) : NavIcon()
    data class DrawableIcon(val resId: Int) : NavIcon()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenScaffold(
    title: String,
    needToGoBack: Boolean = false,
    onBackClick: () -> Unit,
    onMenuClick: () -> Unit,
    content: @Composable (Modifier) -> Unit
) {
//    forma que a topbar funciona (nesse caso ela some se scrollar pra baixo e volta se scrollar um pouco pra cima)
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    // mocka os itens da navbar do usuario
    // futuramente vamos precisar validar o usuario (se é normal ou professor) para escolher entre esses itens e os itens do professor
    val itemsUser = listOf(
        /* HOME */
        BottomNavigationItem(
            title = "Home",
            selectedIcon = NavIcon.VectorIcon(Icons.Filled.Home),
            unselectedIcon = NavIcon.VectorIcon(Icons.Outlined.Home),
            hasNews = false
            /* badgeCount = null (valor padrão)*/
        ),
        /* SEARCH */
        BottomNavigationItem(
            title = "Search",
            selectedIcon = NavIcon.VectorIcon(Icons.Filled.Search),
            unselectedIcon = NavIcon.VectorIcon(Icons.Outlined.Search),
            hasNews = false
            /* badgeCount = null (valor padrão)*/
        ),
        /* SCAN QR CODE */
        BottomNavigationItem(
            title = "QR Code",
            selectedIcon = NavIcon.DrawableIcon(R.drawable.qr_code_icon),
            unselectedIcon = NavIcon.DrawableIcon(R.drawable.qr_code_icon),
            hasNews = false
            /* badgeCount = null (valor padrão)*/
        ),
        /* CHAT */
        BottomNavigationItem(
            title = "ChatBot",
            selectedIcon = NavIcon.DrawableIcon(R.drawable.chat_icon_filled),
            unselectedIcon = NavIcon.DrawableIcon(R.drawable.chat_icon_outlined),
            hasNews = false
            /* badgeCount = null (valor padrão)*/
        ),
        /* PROFILE */
        BottomNavigationItem(
            title = "ChatBot",
            selectedIcon = NavIcon.VectorIcon(Icons.Filled.Person),
            unselectedIcon = NavIcon.VectorIcon(Icons.Outlined.Person),
            hasNews = false
            /* badgeCount = null (valor padrão)*/
        )
    )

    // os itens da navbar aparecem selecionados de acordo com o seu index
    // ex: Cliquei na home, o index atual é 0
    var selectedItemIndex by rememberSaveable {
        mutableIntStateOf(0) // index da tela inicial da navbar (HOME)
    }


    Scaffold (

        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            if (needToGoBack) {

                CenterAlignedTopAppBar(
                    title = { Text(title) }
                    ,
                    navigationIcon = {
                        IconButton(onClick = { onBackClick() }) {
                            Icon(Icons.Default.KeyboardArrowLeft, contentDescription = "Voltar para a tela anterior.")
                        }
                    },
                    actions = {
                        IconButton(onClick = { onMenuClick() }) {
                            Icon(Icons.Default.Menu, contentDescription = "Abrir menu lateral.")
                        }
                    },
                    scrollBehavior = scrollBehavior

                )
            } else {
                TopAppBar(
                    title = { Text(title) }
                    ,
                    actions = {
                        IconButton(onClick = { onMenuClick() }) {
                            Icon(Icons.Default.Menu, contentDescription = "Abrir menu lateral.")
                        }
                    },
                    scrollBehavior = scrollBehavior

                )
            }
        },
        bottomBar = {
            NavigationBar {
                itemsUser.forEachIndexed { index, item ->
                    NavigationBarItem(
                        selected = selectedItemIndex == index,
                        onClick = {
                            selectedItemIndex = index
                            // navController.navigate(item.title)
                        },
                        label = {
                            Text(item.title)
                        },
                        alwaysShowLabel = true,
                        icon = {
                            BadgedBox(
                                badge = {
                                    when {
                                        item.badgeCount != null -> {
                                            Badge {
                                                Text(text = item.badgeCount.toString())
                                            }
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
                                        Icon(
                                            imageVector = iconToShow.icon,
                                            contentDescription = item.title
                                        )
                                    }

                                    is NavIcon.DrawableIcon -> {
                                        Icon(
                                            painter = painterResource(id = iconToShow.resId),
                                            contentDescription = item.title
                                        )
                                    }
                                }
                            }
                        }
                    )
                }
            }


        }


    ) { innerPadding ->
        content(Modifier.padding(innerPadding))
    }
}
//asdasda