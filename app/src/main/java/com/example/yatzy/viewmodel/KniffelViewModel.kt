package com.example.yatzy.viewmodel

import androidx.lifecycle.ViewModel
import com.example.yatzy.model.GameState
import com.example.yatzy.model.Player
import com.example.yatzy.model.ScoreCard
import com.example.yatzy.model.ScoreCategory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class KniffelViewModel : ViewModel() {

    private val _state = MutableStateFlow(GameState())
    val state: StateFlow<GameState> = _state.asStateFlow()

    fun startWithPlayers(names: List<String>) {
        val newPlayers = names.map { name -> Player(name = name) }
        _state.update { it.copy(
            players = newPlayers,
            isSetupFinished = true // isGameStarted wurde hier entfernt
        ) }
    }

    // points: Int? wurde zu points: Int geändert
    // In KniffelViewModel.kt
    fun updateScore(playerId: String, category: ScoreCategory, points: Int?) { // Int? erlaubt null
        _state.update { currentState ->
            currentState.copy(
                players = currentState.players.map { player ->
                    if (player.id == playerId) {
                        val updatedScores = player.scoreCard.scores.toMutableMap().apply {
                            // Wenn points null ist, wird der Eintrag für diese Kategorie entfernt
                            if (points == null) {
                                remove(category)
                            } else {
                                put(category, points)
                            }
                        }
                        player.copy(scoreCard = ScoreCard(scores = updatedScores))
                    } else {
                        player
                    }
                }
            )
        }
    }
}