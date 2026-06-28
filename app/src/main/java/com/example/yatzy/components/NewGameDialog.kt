package com.example.yatzy.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewGameDialog(
    onStartGame: (List<String>) -> Unit,
    onDismiss: () -> Unit
) {
    var playerCount by remember { mutableIntStateOf(2) }
    // Liste von Namen, die sich je nach Spieleranzahl anpasst
    var playerNames by remember { mutableStateOf(listOf("", "")) }

    BasicAlertDialog(
        onDismissRequest = onDismiss,
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .background(Color.White, shape = RoundedCornerShape(24.dp))
            .padding(24.dp),
        properties = DialogProperties(usePlatformDefaultWidth = false),
        content = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                // Header
                Text(
                    "New Game",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "Select the number of players and enter their names.",
                    textAlign = TextAlign.Center,
                    color = Color.Gray,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Number of Players Selector
                Text(
                    "Number of Players",
                    modifier = Modifier.fillMaxWidth(),
                    fontWeight = FontWeight.Medium
                )
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    (1..4).forEach { count ->
                        val isSelected = playerCount == count
                        Surface(
                            modifier = Modifier.weight(1f).height(48.dp).clickable {
                                playerCount = count
                                playerNames = List(count) { i -> playerNames.getOrElse(i) { "" } }
                            },
                            shape = RoundedCornerShape(8.dp),
                            border = BorderStroke(1.dp, Color.LightGray),
                            color = if (isSelected) Color(0xFFEA580C) else Color.White
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    count.toString(),
                                    color = if (isSelected) Color.White else Color.Black
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Player Names Inputs
                Text(
                    "Player Names",
                    modifier = Modifier.fillMaxWidth(),
                    fontWeight = FontWeight.Medium
                )
                playerNames.forEachIndexed { index, name ->
                    OutlinedTextField(
                        value = name,
                        onValueChange = { newName ->
                            val newList = playerNames.toMutableList()
                            newList[index] = newName
                            playerNames = newList
                        },
                        placeholder = { Text("Player ${index + 1}") },
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = TextFieldDefaults.colors(
                            unfocusedContainerColor = Color(0xFFF3F3F5),
                            focusedContainerColor = Color(0xFFF3F3F5)
                        )
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Start Button
                Button(
                    onClick = {
                        onStartGame(playerNames.map {
                            it.ifBlank {
                                "Player ${
                                    playerNames.indexOf(
                                        it
                                    ) + 1
                                }"
                            }
                        })
                    },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9333EA))
                ) {
                    Text("Start Game", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    )
}