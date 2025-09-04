package com.example.clumsychicken.domain.usecase

import com.example.clumsychicken.domain.repository.HighScoreRepository

class ObserveHighScoreUseCase(private val repo: HighScoreRepository) {
    operator fun invoke() = repo.observeHighScore()
}