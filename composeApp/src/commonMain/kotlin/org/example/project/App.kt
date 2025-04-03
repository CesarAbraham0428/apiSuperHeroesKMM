package org.example.project

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import network.NetworkUtils.httpClient
import network.model.ApiResponse
import network.model.Hero
import network.model.SuperheroCard
import network.model.SuperheroDetail
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    MaterialTheme(
        colorScheme = darkColorScheme().copy(
            primary = Color(0xFF1976D2),
            onPrimary = Color.White,
            surface = Color(0xFFF5F5F5),
            background = Color(0xFFF5F5F5)
        )
    ) {
        var superheroName by remember { mutableStateOf("") }
        var superheroList by remember { mutableStateOf<List<Hero>>(emptyList()) }
        var selectedHero by remember { mutableStateOf<Hero?>(null) }
        var isLoading by remember { mutableStateOf(false) }
        var hasSearched by remember { mutableStateOf(false) }
        var errorMessage by remember { mutableStateOf<String?>(null) }

        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier.fillMaxSize().padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // App Header
                Text(
                    text = "Superhero Explorer",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(vertical = 16.dp)
                )
                
                // Search Bar
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextField(
                            value = superheroName,
                            onValueChange = { superheroName = it },
                            modifier = Modifier.weight(1f),
                            placeholder = { Text("Search heroes...") },
                            singleLine = true,
                            colors = TextFieldDefaults.colors(
                                focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                                unfocusedIndicatorColor = Color.LightGray,
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White
                            )
                        )
                        
                        Spacer(modifier = Modifier.width(8.dp))
                        
                        Button(
                            onClick = { 
                                errorMessage = null
                                isLoading = true
                                hasSearched = true
                                getSuperheroList(
                                    superheroName = superheroName,
                                    onSuccessResponse = { 
                                        superheroList = it
                                        isLoading = false
                                    },
                                    onError = { 
                                        errorMessage = it
                                        isLoading = false
                                    }
                                )
                            },
                            enabled = superheroName.isNotBlank() && !isLoading,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search"
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Search")
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Loading indicator
                if (isLoading) {
                    Box(
                        modifier = Modifier.fillMaxWidth().height(200.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(48.dp)
                        )
                    }
                }
                
                // Error message
                AnimatedVisibility(
                    visible = errorMessage != null,
                    enter = fadeIn(animationSpec = tween(300)),
                    exit = fadeOut(animationSpec = tween(300))
                ) {
                    errorMessage?.let {
                        Card(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE))
                        ) {
                            Text(
                                text = it,
                                color = Color(0xFFB71C1C),
                                modifier = Modifier.padding(16.dp),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
                
                // Empty state
                if (superheroList.isEmpty() && !isLoading && hasSearched && errorMessage == null) {
                    Box(
                        modifier = Modifier.fillMaxWidth().height(200.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No heroes found. Try a different search term.",
                            color = Color.Gray,
                            textAlign = TextAlign.Center
                        )
                    }
                }
                
                // Hero list
                if (superheroList.isNotEmpty()) {
                    Text(
                        text = "Found ${superheroList.size} heroes",
                        fontWeight = FontWeight.Medium,
                        color = Color.DarkGray,
                        modifier = Modifier.padding(vertical = 8.dp).align(Alignment.Start)
                    )
                    
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(superheroList) { hero ->
                            SuperheroCard(hero) { selectedHero = hero }
                        }
                    }
                }
            }
            
            // Hero detail dialog
            selectedHero?.let { hero ->
                SuperheroDetail(hero = hero, onDismiss = { selectedHero = null })
            }
        }
    }
}

fun getSuperheroList(
    superheroName: String, 
    onSuccessResponse: (List<Hero>) -> Unit,
    onError: (String) -> Unit
) {
    if(superheroName.isBlank()) return
    val url = "https://www.superheroapi.com/api.php/9873024bb43444abf177320a637b583b/search/$superheroName"

    CoroutineScope(Dispatchers.IO).launch {
        try{
            val response = httpClient.get(url).body<ApiResponse>()
            if (response.ok == "success" && response.results.isNotEmpty()) {
                onSuccessResponse(response.results)
            } else {
                onError("No heroes found with that name")
            }
        } catch (e: Exception){
            println("Error al obtener datos: ${e.message}")
            onError("Error loading heroes: ${e.message}")
        }
    }
}