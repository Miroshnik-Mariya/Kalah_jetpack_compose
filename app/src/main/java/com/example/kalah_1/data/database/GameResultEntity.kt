package com.example.kalah_1.data.database

import androidx.room3.Entity
import androidx.room3.PrimaryKey
import java.util.Date

@Entity(tableName = "game_results")
data class GameResultEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val player1Name: String,
    val player1AvatarRes: Int,
    val player2Name: String,
    val player2AvatarRes: Int,
    val winner: String, // "Player1", "Player2", "Tie"
    val player1Score: Int,
    val player2Score: Int,
    val gameMode: String, // "TwoPlayers" or "VsAI"
    val date: Date,
    val pitsCount: Int,
    val stonesPerPit: Int
)