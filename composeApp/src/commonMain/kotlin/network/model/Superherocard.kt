package network.model

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import network.model.Hero

@Composable
fun SuperheroCard(hero: Hero, onClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Hero image with circular shape and gradient border
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                Color(0xFF1976D2),
                                Color(0xFF64B5F6)
                            )
                        ),
                        shape = CircleShape
                    )
                    .padding(3.dp)
            ) {
                KamelImage(
                    resource = asyncPainterResource(hero.image.url),
                    contentDescription = "Image of ${hero.name}",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Hero information
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = hero.name,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1976D2),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                // Power stats with visual indicators
                PowerStatBar(
                    label = "Intelligence",
                    value = hero.powerstats.intelligence,
                    color = Color(0xFF4CAF50)
                )
                
                PowerStatBar(
                    label = "Strength",
                    value = hero.powerstats.strength,
                    color = Color(0xFFF44336)
                )
                
                PowerStatBar(
                    label = "Speed",
                    value = hero.powerstats.speed,
                    color = Color(0xFFFFEB3B)
                )
            }
        }
    }
}

@Composable
private fun PowerStatBar(label: String, value: String, color: Color) {
    val numericValue = value.toIntOrNull() ?: 0
    val percentage = (numericValue / 100f).coerceIn(0f, 1f)
    
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 2.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.DarkGray,
            modifier = Modifier.width(90.dp)
        )
        
        Spacer(modifier = Modifier.width(8.dp))
        
        Box(
            modifier = Modifier
                .weight(1f)
                .height(8.dp)
                .background(Color.LightGray, RoundedCornerShape(4.dp))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(percentage)
                    .background(color, RoundedCornerShape(4.dp))
            )
        }
        
        Spacer(modifier = Modifier.width(8.dp))
        
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = Color.DarkGray
        )
    }
}
