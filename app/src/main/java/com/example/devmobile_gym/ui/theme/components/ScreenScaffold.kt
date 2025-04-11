package com.example.devmobile_gym.ui.theme.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenScaffold(
    title: String,
    needToGoBack: Boolean = false,
    onBackClick: () -> Unit,
    onMenuClick: () -> Unit,
    content: @Composable (Modifier) -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

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
        }

    ) { innerPadding ->
        content(Modifier.padding(innerPadding))
    }
}
//asdasda