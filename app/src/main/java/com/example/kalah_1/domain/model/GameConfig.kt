package com.example.kalah_1.domain.model

data class GameConfig(
    val mode: GameMode,
    val pitsPerPlayer: Int = 6, // от 6 до 8
    val stonesPerPit: Int = 4,   // от 4 до 6
    val aiDifficulty: Int = 1    // 1-3, только для VsAI
)