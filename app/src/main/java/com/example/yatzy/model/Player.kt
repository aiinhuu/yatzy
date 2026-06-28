package com.example.yatzy.model

data class Player(
    val id: String = java.util.UUID.randomUUID().toString(),
    val name: String,
    val scoreCard: ScoreCard = ScoreCard()
)