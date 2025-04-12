package com.example.devmobile_gym.ui.theme

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.wear.compose.material3.ScreenScaffold
import com.example.devmobile_gym.navigation.AppNavHost
import com.example.devmobile_gym.ui.theme.components.CustomScreenScaffold

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DevMobilegymTheme {
               AppNavHost()
            }
        }
    }
}