package com.example.kalah_1.data.database

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface GameResultDao {
    @Insert
    suspend fun insertResult(result: GameResultEntity)

    @Query("SELECT * FROM game_results ORDER BY date DESC")
    fun getAllResults(): Flow<List<GameResultEntity>>

    @Query("DELETE FROM game_results")
    suspend fun deleteAllResults()
}