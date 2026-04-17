package com.example.kalah_1.domain.model

data class Player (
    val id: String,
    val name: String,
    val avatarResId: Int,
    val isAI: Boolean = false
)