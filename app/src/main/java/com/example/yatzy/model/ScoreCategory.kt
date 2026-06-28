package com.example.yatzy.model

enum class ScoreCategory(val displayName: String, val isUpper: Boolean) {
    ACES("Aces", true),
    TWOS("Twos", true),
    THREES("Threes", true),
    FOURS("Fours", true),
    FIVES("Fives", true),
    SIXES("Sixes", true),
    THREE_OF_A_KIND("3 of a Kind", false),
    FOUR_OF_A_KIND("4 of a Kind", false),
    FULL_HOUSE("Full House", false),
    SMALL_STRAIGHT("Small Straight", false),
    LARGE_STRAIGHT("Large Straight", false),
    YAHTZEE("Yahtzee", false),
    CHANCE("Chance", false)
}