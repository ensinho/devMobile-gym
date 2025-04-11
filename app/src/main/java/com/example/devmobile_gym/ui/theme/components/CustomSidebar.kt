package com.example.devmobile_gym.ui.theme.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight

@Composable
fun SidebarMenu(onMenuItemClick: (String) -> Unit) {
    val menuItems = listOf(
        MenuItem("Página inicial", Icons.Default.Home),
        //MenuItem("Agendar aulas", Icons.Default.CalendarToday),
        //MenuItem("Chatbot", Icons.Default.Chat),
        MenuItem("Gerenciar", Icons.Default.Build),
        MenuItem("Perfil", Icons.Default.Person)
    )

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .width(200.dp)
            .background(Color(0xFF1C1C1E), shape = RoundedCornerShape(topEnd = 20.dp, bottomEnd = 20.dp))
            .padding(vertical = 24.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "Menu",
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))

        menuItems.forEach { item ->
            SidebarItem(item = item, onClick = { onMenuItemClick(item.label) })
        }
    }
}

@Composable
fun SidebarItem(item: MenuItem, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 10.dp)
            .background(Color.Transparent)
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = item.icon,
            contentDescription = item.label,
            tint = Color.White,
            modifier = Modifier.size(22.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = item.label,
            color = Color.White,
            fontSize = 15.sp
        )
    }
}

data class MenuItem(val label: String, val icon: ImageVector)
