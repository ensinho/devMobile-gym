package com.example.devmobile_gym.presentation.screens.AulasFucionais

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.getValue
import com.example.devmobile_gym.presentation.components.CustomScreenScaffold


@Composable
fun ShowAulas() {

    val viewmodel: AulasViewModel = viewModel()
    val aulas by viewmodel.aulas

    CustomScreenScaffold() { }
}


