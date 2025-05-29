import android.content.Intent
import android.provider.Settings
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AcessibilidadeIconButton(modifier: Modifier = Modifier) {
    val context = LocalContext.current

    IconButton(
        onClick = {
            val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
            context.startActivity(intent)
        },
        modifier = modifier.padding(bottom = 13.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Settings,
            contentDescription = "Abrir configurações de acessibilidade",
            tint = Color.White,
            modifier = Modifier.size(32.dp) // Aumenta o tamanho do ícone
        )
    }

}
