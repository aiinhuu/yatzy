package com.example.yatzy.model

data class ScoreCard(
    // Die Map speichert nur die reinen Eingaben des Users
    val scores: Map<ScoreCategory, Int> = emptyMap()
) {
    // 1. Summe der oberen Sektion (Aces bis Sixes)
    val upperSubtotal: Int
        get() = ScoreCategory.entries
            .filter { it.isUpper }
            .sumOf { category -> scores[category] ?: 0 }

    // 2. Bonus-Logik: 35 Punkte, wenn die Summe oben >= 63 ist
    val upperBonus: Int
        get() = if (upperSubtotal >= 63) 35 else 0

    // 3. Gesamtsumme der oberen Sektion inklusive Bonus
    val upperTotal: Int
        get() = upperSubtotal + upperBonus

    // 4. Summe der unteren Sektion (Dreierpasch bis Chance)
    val lowerTotal: Int
        get() = ScoreCategory.entries
            .filter { !it.isUpper }
            .sumOf { category -> scores[category] ?: 0 }

    // 5. Das finale Endergebnis
    val grandTotal: Int
        get() = upperTotal + lowerTotal
}