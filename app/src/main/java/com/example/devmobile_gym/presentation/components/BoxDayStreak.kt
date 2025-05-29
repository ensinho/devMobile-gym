import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun StreakBox(
    text: String,
    iconResId: Int,
    onClick: () -> Unit,
    color: Color = Color.White,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .padding(8.dp) // Slightly increased padding for a cleaner look
            .fillMaxWidth() // Make it fill the width for better responsiveness
            .clickable(onClick = onClick), // Make the entire card clickable
        colors = CardDefaults.cardColors(containerColor = Color(0xFF3B3B3B)), // Dark background
        shape = RoundedCornerShape(12.dp), // More rounded corners for a modern feel
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp) // Subtle elevation for depth
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start, // Align content to the start
            modifier = Modifier.padding(16.dp) // Generous padding inside the card
        ) {
            Image(
                painter = painterResource(id = iconResId),
                contentDescription = null, // Content description can be null if it's purely decorative
                modifier = Modifier.size(32.dp), // Slightly larger icon
                colorFilter = ColorFilter.tint(color) // Icon colored with accent
            )

            Spacer(modifier = Modifier.width(12.dp)) // More space between icon and text

            Text(
                text = text,
                color = Color.White, // White text for contrast on dark background
                fontSize = 18.sp, // Slightly larger text for readability
                fontWeight = FontWeight.Medium // Medium font weight for a balanced look
            )
        }
    }
}