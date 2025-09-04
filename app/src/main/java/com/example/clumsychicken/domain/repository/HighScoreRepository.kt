package com.example.clumsychicken.domain.repository

import kotlinx.coroutines.flow.Flow

interface HighScoreRepository {
    fun observeHighScore(): Flow<Int>
    suspend fun updateHighScore(score: Int)
}