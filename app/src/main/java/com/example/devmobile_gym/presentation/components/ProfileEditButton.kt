package com.example.devmobile_gym.presentation.components
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun ProfileEditButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    tint: Color = MaterialTheme.colorScheme.onSurface // Cor padrão do ícone
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Icon(
            imageVector = Icons.Filled.Edit, // Ícone de lápis/edição
            contentDescription = "Editar Perfil", // Descrição para acessibilidade
            tint = tint
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewProfileEditButton() {
    MaterialTheme { // Use seu tema para o preview
        ProfileEditButton(onClick = { /* Ação de clique */ })
    }
}