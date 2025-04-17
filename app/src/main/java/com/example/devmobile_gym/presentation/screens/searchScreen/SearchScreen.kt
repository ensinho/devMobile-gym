package com.example.devmobile_gym.presentation.screens.searchScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.devmobile_gym.presentation.components.BoxSeta
import com.example.devmobile_gym.presentation.components.CustomScreenScaffold
import com.example.devmobile_gym.presentation.components.CustomTextField
import com.example.devmobile_gym.presentation.components.ScreenScaffoldPreview
import com.example.devmobile_gym.ui.theme.White

@Composable
fun SearchScreen(viewModel: SearchScreenViewModel = viewModel()) {
    var search = viewModel.search.value

    CustomScreenScaffold(
        title = "Search",
        needToGoBack = false,
        onBackClick = { /*TODO*/ },
        onMenuClick = { /*TODO*/ },
        content = { innerModifier ->

            val combinedModifier = innerModifier.padding(0.5.dp)

            Column (
                modifier = combinedModifier.fillMaxSize()
            ){
                Text(
                    text = "Buscar",
                    fontSize = 30.sp,
                    color = White
                )
                Spacer(Modifier.height(5.dp))
                Row {

                    CustomTextField(
                        label = "Busque máquinas ou exercícios",
                        value = search,
                        onValueChange = viewModel::onSearchChange,
                        padding = 10
                    )

                    BoxSeta()
                }
            }
        }
    )
}