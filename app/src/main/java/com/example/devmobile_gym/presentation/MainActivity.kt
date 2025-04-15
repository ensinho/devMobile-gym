package com.example.devmobile_gym.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.devmobile_gym.navigation.AppNavHost
import com.example.devmobile_gym.ui.theme.DevmobileGymTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DevmobileGymTheme {
                AppNavHost()
            }
        }
    }
}