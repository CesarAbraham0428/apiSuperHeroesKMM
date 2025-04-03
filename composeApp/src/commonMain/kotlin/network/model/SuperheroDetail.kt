package network.model

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource

@Composable
fun SuperheroDetail(hero: Hero, onDismiss: () -> Unit) {
    Dialog(
        onDismissRequest = onDismiss
    ) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
            shadowElevation = 8.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Hero image with circular shape and gradient border
                Box(
                    modifier = Modifier
                        .size(150.dp)
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    Color(0xFF1976D2),
                                    Color(0xFF64B5F6)
                                )
                            ),
                            shape = CircleShape
                        )
                        .padding(4.dp)
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
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Hero name
                Text(
                    text = hero.name,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1976D2),
                    textAlign = TextAlign.Center
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Power stats section
                Text(
                    text = "Power Stats",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.DarkGray
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Power stats with visual indicators
                DetailStatBar(
                    label = "Intelligence",
                    value = hero.powerstats.intelligence,
                    color = Color(0xFF4CAF50)
                )
                
                DetailStatBar(
                    label = "Strength",
                    value = hero.powerstats.strength,
                    color = Color(0xFFF44336)
                )
                
                DetailStatBar(
                    label = "Speed",
                    value = hero.powerstats.speed,
                    color = Color(0xFFFFEB3B)
                )
                
                DetailStatBar(
                    label = "Durability",
                    value = hero.powerstats.durability,
                    color = Color(0xFF9C27B0)
                )
                
                DetailStatBar(
                    label = "Power",
                    value = hero.powerstats.power,
                    color = Color(0xFF2196F3)
                )
                
                DetailStatBar(
                    label = "Combat",
                    value = hero.powerstats.combat,
                    color = Color(0xFFFF9800)
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // ID information
                Text(
                    text = "Hero ID: ${hero.id}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.DarkGray,
                    textAlign = TextAlign.Center
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Close button
                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF1976D2)
                    ),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Close")
                }
            }
        }
    }
}

@Composable
private fun DetailStatBar(label: String, value: String, color: Color) {
    val numericValue = value.toIntOrNull() ?: 0
    val percentage = (numericValue / 100f).coerceIn(0f, 1f)
    
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.DarkGray,
            modifier = Modifier.width(100.dp)
        )
        
        Spacer(modifier = Modifier.width(8.dp))
        
        Box(
            modifier = Modifier
                .weight(1f)
                .height(10.dp)
                .background(Color.LightGray, RoundedCornerShape(5.dp))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(percentage)
                    .background(color, RoundedCornerShape(5.dp))
            )
        }
        
        Spacer(modifier = Modifier.width(8.dp))
        
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = Color.DarkGray,
            modifier = Modifier.width(30.dp)
        )
    }
}
