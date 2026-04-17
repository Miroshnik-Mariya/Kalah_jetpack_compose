package com.example.kalah_1.data.repository

import android.content.Context
import com.example.kalah_1.data.database.GameDatabase
import com.example.kalah_1.data.database.GameResultEntity
import kotlinx.coroutines.flow.Flow

class GameRepository(context: Context) {
    private val database = GameDatabase.getDatabase(context)
    private val dao = database.gameResultDao()

    suspend fun saveResult(result: GameResultEntity) {
        dao.insertResult(result)
    }

    fun getAllResults(): Flow<List<GameResultEntity>> {
        return dao.getAllResults()
    }

    suspend fun clearHistory() {
        dao.deleteAllResults()
    }
}