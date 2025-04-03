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
            color = Color(0xFF1E1E1E),
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
                            Brush.radialGradient(
                                colors = listOf(
                                    Color(0xFF64B5F6),
                                    Color(0xFF1976D2)
                                )
                            ),
                            CircleShape
                        )
                        .padding(4.dp)
                ) {
                    KamelImage(
                        resource = asyncPainterResource(hero.image.url),
                        contentDescription = "Imagen de ${hero.name}",
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
                    color = Color(0xFF64B5F6),
                    textAlign = TextAlign.Center
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Power stats section
                Text(
                    text = "Estadísticas de Poder",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.LightGray
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Power stats with visual indicators
                DetailStatBar(
                    label = "Inteligencia",
                    value = hero.powerstats.intelligence,
                    color = Color(0xFF4CAF50)
                )
                
                DetailStatBar(
                    label = "Fuerza",
                    value = hero.powerstats.strength,
                    color = Color(0xFFF44336)
                )
                
                DetailStatBar(
                    label = "Velocidad",
                    value = hero.powerstats.speed,
                    color = Color(0xFFFFEB3B)
                )
                
                DetailStatBar(
                    label = "Resistencia",
                    value = hero.powerstats.durability,
                    color = Color(0xFF9C27B0)
                )
                
                DetailStatBar(
                    label = "Poder",
                    value = hero.powerstats.power,
                    color = Color(0xFF2196F3)
                )
                
                DetailStatBar(
                    label = "Combate",
                    value = hero.powerstats.combat,
                    color = Color(0xFFFF9800)
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // ID information
                Text(
                    text = "ID del Héroe: ${hero.id}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.LightGray,
                    textAlign = TextAlign.Center
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Close button
                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF64B5F6)
                    ),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Cerrar")
                }
            }
        }
    }
}

@Composable
private fun DetailStatBar(label: String, value: String, color: Color) {
    val numericValue = value.toIntOrNull() ?: 0
    val percentage = (numericValue / 100f).coerceIn(0f, 1f)
    val isHighStat = numericValue >= 80
    
    val rowModifier = if (isHighStat) {
        Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .background(Color(0xFF2A2A2A), RoundedCornerShape(8.dp))
            .padding(8.dp)
    } else {
        Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    }
    
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = rowModifier
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = if (isHighStat) Color.White else Color.LightGray,
            fontWeight = if (isHighStat) FontWeight.Bold else FontWeight.Normal,
            modifier = Modifier.width(100.dp)
        )
        
        Spacer(modifier = Modifier.width(8.dp))
        
        Box(
            modifier = Modifier
                .weight(1f)
                .height(10.dp)
                .background(Color(0xFF333333), RoundedCornerShape(5.dp))
        ) {
            // Usar diferentes enfoques para el fondo según si es una estadística alta o no
            if (isHighStat) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(percentage)
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(color, Color.White)
                            ),
                            RoundedCornerShape(5.dp)
                        )
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(percentage)
                        .background(color, RoundedCornerShape(5.dp))
                )
            }
        }
        
        Spacer(modifier = Modifier.width(8.dp))
        
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = if (isHighStat) FontWeight.ExtraBold else FontWeight.Bold,
            color = if (isHighStat) color else Color.LightGray,
            modifier = Modifier.width(30.dp)
        )
    }
}
