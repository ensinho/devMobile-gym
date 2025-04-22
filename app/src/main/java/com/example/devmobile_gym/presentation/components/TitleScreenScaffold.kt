package com.example.devmobile_gym.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.devmobile_gym.R
@Composable
fun TitleScaffold() {
    Image(
        painter = painterResource(id = R.drawable.tituloscaffold),
        contentDescription = "titulo",
    )
}
