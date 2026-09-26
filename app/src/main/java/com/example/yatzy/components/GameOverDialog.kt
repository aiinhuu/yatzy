package com.example.yatzy.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.yatzy.model.Player

@Composable
fun GameOverDialog(
    players: List<Player>,
    onPlayAgain: () -> Unit
) {
    // Sortiere Spieler absteigend nach ihrer Gesamtpunktzahl
    val sortedPlayers = players.sortedByDescending { it.scoreCard.grandTotal }
    val winner = sortedPlayers.firstOrNull() ?: return

    Dialog(
        onDismissRequest = { /* Blockiert das Schließen durch Klicken daneben */ },
        // Erlaubt dem Dialog, die volle Bildschirmbreite und -höhe einzunehmen
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        // Vollbild-Surface mit dynamischer Hintergrundfarbe (Dark/Light Mode)
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Top Gradient Section (Header)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(Color(0xFFF97316), Color(0xFF8B5CF6)) // Orange zu Lila
                            )
                        )
                        // Extra Padding oben für die Statusleiste des Handys
                        .padding(top = 48.dp, bottom = 32.dp, start = 16.dp, end = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        // Gewinner-Icon (Stern)
                        Surface(
                            shape = CircleShape,
                            color = Color.White.copy(alpha = 0.2f),
                            modifier = Modifier.size(80.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = "Winner",
                                tint = Color(0xFFFDE047), // Gelb
                                modifier = Modifier
                                    .padding(16.dp)
                                    .fillMaxSize()
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Game Over!",
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )

                        Text(
                            text = "Winner: ${winner.name}",
                            fontSize = 18.sp,
                            color = Color.White.copy(alpha = 0.9f),
                            modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
                        )

                        Row(verticalAlignment = Alignment.Bottom) {
                            Text(
                                text = "${winner.scoreCard.grandTotal}",
                                fontSize = 56.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White,
                                lineHeight = 56.sp
                            )
                            Text(
                                text = " points",
                                fontSize = 18.sp,
                                color = Color.White.copy(alpha = 0.8f),
                                modifier = Modifier.padding(bottom = 8.dp, start = 4.dp)
                            )
                        }
                    }
                }

                // Bottom Content Section (Rest des Bildschirms)
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Final Scores",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    // Liste der Spieler-Ergebnisse
                    sortedPlayers.forEachIndexed { index, player ->
                        val isWinner = index == 0

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .border(
                                    width = if (isWinner) 2.dp else 0.dp,
                                    color = if (isWinner) Color(0xFF8B5CF6) else Color.Transparent,
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .background(
                                    // Transparenter Hintergrund für Dark Mode Kompatibilität
                                    color = if (isWinner) Color(0xFF8B5CF6).copy(alpha = 0.1f) else MaterialTheme.colorScheme.surfaceVariant,
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = player.name,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    if (isWinner) {
                                        Surface(
                                            color = Color(0xFF8B5CF6),
                                            shape = RoundedCornerShape(8.dp),
                                            modifier = Modifier.padding(start = 8.dp)
                                        ) {
                                            Text(
                                                text = "WINNER",
                                                color = Color.White,
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold,
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                            )
                                        }
                                    }
                                }
                                Text(
                                    text = "${index + 1}. Place",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                )
                            }

                            Text(
                                text = "${player.scoreCard.grandTotal}",
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 22.sp,
                                color = if (isWinner) Color(0xFF8B5CF6) else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }

                    // Drückt den Button ganz nach unten an den Rand
                    Spacer(modifier = Modifier.weight(1f))

                    // Play Again Button
                    Button(
                        onClick = onPlayAgain,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        contentPadding = PaddingValues(),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    brush = Brush.horizontalGradient(
                                        colors = listOf(Color(0xFFF97316), Color(0xFF8B5CF6))
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Refresh,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Play Again",
                                    color = Color.White,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}