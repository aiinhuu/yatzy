package com.example.yatzy.model

data class GameState(
    val players: List<Player> = emptyList(),
    val isSetupFinished: Boolean = false,
    val currentRound: Int = 1
)