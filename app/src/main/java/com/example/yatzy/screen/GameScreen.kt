package com.example.yatzy.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.yatzy.components.BinaryScoreCard
import com.example.yatzy.components.GameOverDialog
import com.example.yatzy.components.GrandTotalCard
import com.example.yatzy.components.NewGameDialog
import com.example.yatzy.components.NumericInputCard
import com.example.yatzy.components.PlayerBottomBar
import com.example.yatzy.components.ScoreInputCard
import com.example.yatzy.components.UpperSectionSummary
import com.example.yatzy.model.ScoreCategory
import com.example.yatzy.viewmodel.KniffelViewModel

@Composable
fun GameScreen(
    viewModel: KniffelViewModel,
    isDarkMode: Boolean,
    onToggleDarkMode: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    // UI States
    var showSetup by remember { mutableStateOf(!state.isSetupFinished) }
    var selectedPlayerIndex by remember { mutableIntStateOf(0) }
    var showGameOverDialog by remember { mutableStateOf(false) }

    // Der aktuell ausgewählte Spieler basierend auf dem Tab unten
    val currentPlayer = state.players.getOrNull(selectedPlayerIndex)

    // Hintergrund-Check für das Spielende (Jeder Spieler hat 13 Felder ausgefüllt)
//    val isGameOver by remember(state.players) {
//        derivedStateOf {
//            state.players.isNotEmpty() && state.players.all { player ->
//                player.scoreCard.scores.values.count { it != null } >= 13
//            }
//        }
//    }

    val isGameOver by remember(state.players) {
        derivedStateOf {
            state.players.isNotEmpty() && state.players.all { player ->
                val filledSlots = player.scoreCard.scores.values.count { it != null }
                // HIER ÄNDERN: Für den Test reicht ein einziges ausgefülltes Feld
                filledSlots >= 1
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            topBar = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(16.dp, vertical = 0.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "KNIFFEL",
                        fontSize = 50.sp,
                        lineHeight = 50.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.displayLarge
                    )

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = onToggleDarkMode) {
                            Icon(
                                imageVector = if (isDarkMode) Icons.Default.LightMode else Icons.Default.DarkMode,
                                contentDescription = "Toggle Dark Mode",
                                tint = Color(0xFF6B7280)
                            )
                        }
                    }
                }
            }, bottomBar = {
                if (state.players.isNotEmpty()) {
                    PlayerBottomBar(
                        players = state.players,
                        selectedIndex = selectedPlayerIndex,
                        onPlayerSelected = { index ->
                            selectedPlayerIndex = index
                        })
                }
            }) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(MaterialTheme.colorScheme.background)
            ) {
                // 1. Festes Element: Grand Total Card für den aktuellen Spieler
                // Da es außerhalb der LazyColumn liegt, scrollt es nicht mit!
                GrandTotalCard(
                    playerName = currentPlayer?.name ?: "Player",
                )

                // 2. Scrollbarer Bereich für den Rest des Spiels
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 32.dp)
                ) {

                    // 2. UPPER SECTION
                    item {
                        Text(
                            text = "Upper Section",
                            modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 8.dp),
                            style = MaterialTheme.typography.titleMedium,
                            fontSize = 26.sp,
                            color = Color(0xFFEA580C),
                            fontWeight = FontWeight.Bold
                        )
                    }

                    val upperCategories = ScoreCategory.entries.filter { it.isUpper }
                    itemsIndexed(upperCategories) { index, category ->
                        val multiplier = index + 1
                        val possibleScores = (1..5).map { it * multiplier }
                        val currentValue = currentPlayer?.scoreCard?.scores?.get(category)

                        Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                            ScoreInputCard(
                                categoryName = category.displayName,
                                possibleScores = possibleScores,
                                currentValue = currentValue,
                                onScoreSelected = { points ->
                                    currentPlayer?.let {
                                        viewModel.updateScore(
                                            it.id, category, points
                                        )
                                    }
                                },
                                onCrossOut = {
                                    currentPlayer?.let { viewModel.updateScore(it.id, category, 0) }
                                })
                        }

                        if (index < upperCategories.size - 1) {
                            HorizontalDivider(
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                                thickness = 1.dp,
                                color = Color(0xFFF3F3F5)
                            )
                        }
                    }

                    // Upper Section Zusammenfassung (Punkte und Bonus)
                    item {
                        Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                            UpperSectionSummary(
                                subtotal = currentPlayer?.scoreCard?.upperSubtotal ?: 0,
                                bonus = currentPlayer?.scoreCard?.upperBonus ?: 0
                            )
                        }
                    }

                    // 3. LOWER SECTION
                    item {
                        Text(
                            text = "Lower Section",
                            modifier = Modifier.padding(start = 16.dp, top = 24.dp, bottom = 8.dp),
                            style = MaterialTheme.typography.titleMedium,
                            fontSize = 26.sp,
                            color = Color(0xFFEA580C),
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Numeric Inputs (Pasch & Chance)
                    item {
                        Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                            NumericInputCard(
                                categoryName = "3 of a Kind",
                                currentValue = currentPlayer?.scoreCard?.scores?.get(ScoreCategory.THREE_OF_A_KIND),
                                onScoreSelected = { points ->
                                    currentPlayer?.let {
                                        viewModel.updateScore(
                                            it.id, ScoreCategory.THREE_OF_A_KIND, points
                                        )
                                    }
                                },
                                onCrossOut = {
                                    currentPlayer?.let {
                                        viewModel.updateScore(
                                            it.id, ScoreCategory.THREE_OF_A_KIND, 0
                                        )
                                    }
                                })
                        }
                        HorizontalDivider(
                            modifier = Modifier.padding(
                                horizontal = 16.dp, vertical = 8.dp
                            ), color = Color(0xFFF3F3F5)
                        )
                    }

                    item {
                        Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                            NumericInputCard(
                                categoryName = "4 of a Kind",
                                currentValue = currentPlayer?.scoreCard?.scores?.get(ScoreCategory.FOUR_OF_A_KIND),
                                onScoreSelected = { points ->
                                    currentPlayer?.let {
                                        viewModel.updateScore(
                                            it.id, ScoreCategory.FOUR_OF_A_KIND, points
                                        )
                                    }
                                },
                                onCrossOut = {
                                    currentPlayer?.let {
                                        viewModel.updateScore(
                                            it.id, ScoreCategory.FOUR_OF_A_KIND, 0
                                        )
                                    }
                                })
                        }
                        HorizontalDivider(
                            modifier = Modifier.padding(
                                horizontal = 16.dp, vertical = 8.dp
                            ), color = Color(0xFFF3F3F5)
                        )
                    }

                    // Binary Inputs (Full House, Straights, Yahtzee)
                    item {
                        Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                            BinaryScoreCard(
                                categoryName = "Full House",
                                scoreValue = 25,
                                currentValue = currentPlayer?.scoreCard?.scores?.get(ScoreCategory.FULL_HOUSE),
                                onScoreSelected = { points ->
                                    currentPlayer?.let {
                                        viewModel.updateScore(
                                            it.id, ScoreCategory.FULL_HOUSE, points
                                        )
                                    }
                                })
                        }
                        HorizontalDivider(
                            modifier = Modifier.padding(
                                horizontal = 16.dp, vertical = 8.dp
                            ), color = Color(0xFFF3F3F5)
                        )
                    }

                    item {
                        Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                            BinaryScoreCard(
                                categoryName = "Small Straight",
                                scoreValue = 30,
                                currentValue = currentPlayer?.scoreCard?.scores?.get(ScoreCategory.SMALL_STRAIGHT),
                                onScoreSelected = { points ->
                                    currentPlayer?.let {
                                        viewModel.updateScore(
                                            it.id, ScoreCategory.SMALL_STRAIGHT, points
                                        )
                                    }
                                })
                        }
                        HorizontalDivider(
                            modifier = Modifier.padding(
                                horizontal = 16.dp, vertical = 8.dp
                            ), color = Color(0xFFF3F3F5)
                        )
                    }

                    item {
                        Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                            BinaryScoreCard(
                                categoryName = "Large Straight",
                                scoreValue = 40,
                                currentValue = currentPlayer?.scoreCard?.scores?.get(ScoreCategory.LARGE_STRAIGHT),
                                onScoreSelected = { points ->
                                    currentPlayer?.let {
                                        viewModel.updateScore(
                                            it.id, ScoreCategory.LARGE_STRAIGHT, points
                                        )
                                    }
                                })
                        }
                        HorizontalDivider(
                            modifier = Modifier.padding(
                                horizontal = 16.dp, vertical = 8.dp
                            ), color = Color(0xFFF3F3F5)
                        )
                    }

                    item {
                        Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                            BinaryScoreCard(
                                categoryName = "Kniffel",
                                scoreValue = 50,
                                currentValue = currentPlayer?.scoreCard?.scores?.get(ScoreCategory.YAHTZEE),
                                onScoreSelected = { points ->
                                    currentPlayer?.let {
                                        viewModel.updateScore(
                                            it.id, ScoreCategory.YAHTZEE, points
                                        )
                                    }
                                })
                        }
                        HorizontalDivider(
                            modifier = Modifier.padding(
                                horizontal = 16.dp, vertical = 8.dp
                            ), color = Color(0xFFF3F3F5)
                        )
                    }

                    item {
                        Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                            NumericInputCard(
                                categoryName = "Chance",
                                currentValue = currentPlayer?.scoreCard?.scores?.get(ScoreCategory.CHANCE),
                                onScoreSelected = { points ->
                                    currentPlayer?.let {
                                        viewModel.updateScore(
                                            it.id, ScoreCategory.CHANCE, points
                                        )
                                    }
                                },
                                onCrossOut = {
                                    currentPlayer?.let {
                                        viewModel.updateScore(
                                            it.id, ScoreCategory.CHANCE, 0
                                        )
                                    }
                                })
                        }
                    }
                    if (isGameOver) {
                        item {
                            Button(
                                onClick = { showGameOverDialog = true },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(
                                    0xFF3646D0
                                )
                                ), // Grün für Abschluss
                                shape = RoundedCornerShape(12.dp),
                                contentPadding = PaddingValues(16.dp)
                            ) {
                                Text(
                                    text = "Show Results",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            }

            // --- OVERLAYS: SETUP DIALOG & GAME OVER ---

            if (showSetup) { // Oder wie dein State für den Setup-Screen heißt
                val lastPlayerNames = state.players.map { it.name }

                NewGameDialog(
                    initialPlayers = lastPlayerNames,
                    onStartGame = { names ->
                        // Hier nutzen wir jetzt den echten Namen aus deinem ViewModel:
                        viewModel.startWithPlayers(names)
                        showSetup = false
                    },
                    onDismiss = { })
            }

            if (showGameOverDialog) {
                GameOverDialog(
                    players = state.players,
                    onPlayAgain = {
                        showGameOverDialog = false // Wichtig: Dialog wieder schließen
                        showSetup = true
                    }
                )
            }
        }
    }
}