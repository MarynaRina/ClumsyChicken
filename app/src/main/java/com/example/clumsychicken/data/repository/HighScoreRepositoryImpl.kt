package com.example.clumsychicken.data.repository

import com.example.clumsychicken.data.prefs.HighScoreDataSource
import com.example.clumsychicken.domain.repository.HighScoreRepository
import kotlinx.coroutines.flow.Flow

class HighScoreRepositoryImpl(private val ds: HighScoreDataSource) : HighScoreRepository {
    override fun observeHighScore(): Flow<Int> = ds.highScore
    override suspend fun updateHighScore(score: Int) = ds.setHigh(score)
}