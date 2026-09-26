package com.example.yatzy.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.yatzy.R

@Composable
fun NewGameDialog(
    initialPlayers: List<String> = emptyList(),
    onStartGame: (List<String>) -> Unit,
    onDismiss: () -> Unit
) {
    val players = remember(initialPlayers) {
        val startList = if (initialPlayers.isNotEmpty()) initialPlayers else listOf("", "")
        mutableStateListOf(*startList.toTypedArray())
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Box(modifier = Modifier.fillMaxSize()) {

                // 1. Erster Würfel (Oben Rechts)
                Icon(
                    painter = painterResource(id = R.drawable.ic_dice),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.05f),
                    modifier = Modifier
                        .size(350.dp)
                        .offset(x = 80.dp, y = (-30).dp)
                        .rotate(20f)
                        .align(Alignment.TopEnd)
                )

                // 2. Zweiter Würfel (Mitte Links)
                Icon(
                    painter = painterResource(id = R.drawable.ic_dice),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.05f),
                    modifier = Modifier
                        .size(280.dp)
                        .offset(x = (-60).dp, y = 20.dp)
                        .rotate(-15f)
                        .align(Alignment.CenterStart)
                )

                // 3. Dritter Würfel (Unten Rechts)
                Icon(
                    painter = painterResource(id = R.drawable.ic_dice),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.05f),
                    modifier = Modifier
                        .size(320.dp)
                        .offset(x = 50.dp, y = 60.dp)
                        .rotate(45f)
                        .align(Alignment.BottomEnd)
                )

                // Der eigentliche Inhalt der Seite
                Column(modifier = Modifier.fillMaxSize()) {
                    // Header Sektion
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 64.dp, bottom = 32.dp, start = 16.dp, end = 16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "KNIFFEL",
                            fontSize = 80.sp,
                            fontWeight = FontWeight.ExtraBold,
                            style = MaterialTheme.typography.displayLarge,
                            color = MaterialTheme.colorScheme.primary,
                            letterSpacing = 2.sp
                        )
                    }

                    // Inhaltsbereich (Liste der Spieler)
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp)
                    ) {
                        Text(
                            text = "Players",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )

                        LazyColumn(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            itemsIndexed(players) { index, name ->
                                OutlinedTextField(
                                    value = name,
                                    onValueChange = { newName -> players[index] = newName },
                                    label = { Text("Player ${index + 1}") },
                                    modifier = Modifier.fillMaxWidth(),
                                    singleLine = true,
                                    shape = RoundedCornerShape(12.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = Color(0xFFEA580C),
                                        unfocusedBorderColor = MaterialTheme.colorScheme.outline
                                    ),
                                    trailingIcon = {
                                        if (players.size > 1) {
                                            IconButton(onClick = { players.removeAt(index) }) {
                                                Icon(
                                                    imageVector = Icons.Default.Close,
                                                    contentDescription = "Remove Player",
                                                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                                                )
                                            }
                                        }
                                    }
                                )
                            }

                            item {
                                TextButton(
                                    // Hier wird jetzt auch ein leeres Feld hinzugefügt
                                    onClick = { players.add("") },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 8.dp)
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.Add,
                                            contentDescription = "Add Player",
                                            tint = MaterialTheme.colorScheme.primary
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = "Add Another Player",
                                            color = MaterialTheme.colorScheme.primary,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }

                        // Start Game Button
                        Button(
                            onClick = {
                                // Filtert leere Textfelder automatisch heraus
                                val validPlayers = players.filter { it.isNotBlank() }
                                if (validPlayers.isNotEmpty()) {
                                    onStartGame(validPlayers)
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                                .padding(top = 16.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFEA580C),
                                contentColor = Color.White
                            ),
                            // Button ist nur aktiv, wenn mindestens ein Name eingegeben wurde
                            enabled = players.any { it.isNotBlank() }
                        ) {
                            Text(
                                text = "Start Game",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}