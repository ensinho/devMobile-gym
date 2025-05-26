package com.example.devmobile_gym.presentation

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import android.net.Uri
import com.example.devmobile_gym.domain.model.Exercicio
import com.example.devmobile_gym.navigation.AppNavHost
import com.example.devmobile_gym.ui.theme.DevmobileGymTheme
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.handleCoroutineException
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import androidx.core.net.toUri


class MainActivity : ComponentActivity() {

    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DevmobileGymTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    val scope =
                        rememberCoroutineScope() // CoroutineScope para operações assíncronas

                    // Manipula o resultado do QR code escaneado
                    val handleQRCodeResult: (String) -> Unit = { result ->
                        Toast.makeText(
                            this@MainActivity,
                            "QR Code lido: $result",
                            Toast.LENGTH_LONG
                        ).show()

                        // CENÁRIO 1: QR code contém diretamente a URL do YouTube
                        if (result.startsWith("http://") || result.startsWith("https://")) {
                            try {
                                val intent = Intent(Intent.ACTION_VIEW, result.toUri())
                                startActivity(intent)
                            } catch (e: Exception) {
                                Toast.makeText(
                                    this@MainActivity,
                                    "Não foi possível abrir o link",
                                    Toast.LENGTH_SHORT
                                ).show()
                                Log.e("QRCode", "Erro ao abrir link diretamente: ${e.message}")
                            }
                        } else {
                            // CENÁRIO 2: QR code contém o ID do Exercício do Firebase
                            // Assumimos que o QR Code lido é o ID de um documento na coleção "exercicios"
                            scope.launch {
                                try {
                                    val exercicioRef = db.collection("exercicios").document(result)
                                    val snapshot =
                                        exercicioRef.get().await() // Busca o documento no Firestore
                                    val exercicio =
                                        snapshot.toObject(Exercicio::class.java) // Converte para o modelo Exercicio

                                    exercicio?.linkVideo?.let { youtubeLink ->
                                        // Abre o link do YouTube encontrado
                                        val intent =
                                            Intent(Intent.ACTION_VIEW, youtubeLink.toUri())
                                        startActivity(intent)
                                    } ?: run {
                                        // Se o link do vídeo não for encontrado
                                        Toast.makeText(
                                            this@MainActivity,
                                            "Link do vídeo não encontrado para este exercício.",
                                            Toast.LENGTH_LONG
                                        ).show()
                                        Log.w(
                                            "QRCode",
                                            "Link do vídeo nulo para o ID do exercício: $result"
                                        )
                                    }

                                } catch (e: Exception) {
                                    Toast.makeText(
                                        this@MainActivity,
                                        "Erro ao buscar exercício: ${e.message}",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    Log.e(
                                        "QRCode",
                                        "Erro ao buscar exercício no Firebase com ID '$result': ${e.message}",
                                        e
                                    )
                                }
                            }
                        }
                    }

                    AppNavHost(
                        navController = navController,
                        onQRCodeScanned = handleQRCodeResult
                    )

                }

            }
        }
    }
}
