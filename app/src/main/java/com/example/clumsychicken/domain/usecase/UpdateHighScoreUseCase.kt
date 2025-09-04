package com.example.clumsychicken.domain.usecase

import com.example.clumsychicken.domain.repository.HighScoreRepository

class UpdateHighScoreUseCase(private val repo: HighScoreRepository) {
    suspend operator fun invoke(score: Int) = repo.updateHighScore(score)
}