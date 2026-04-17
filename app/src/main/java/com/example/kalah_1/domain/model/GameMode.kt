package com.example.kalah_1.domain.model

sealed class GameMode {
    object TwoPlayers : GameMode()
    object VsAI : GameMode()
}
