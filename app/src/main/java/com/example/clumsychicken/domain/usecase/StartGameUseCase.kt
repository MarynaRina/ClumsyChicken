package com.example.clumsychicken.domain.usecase

import com.example.clumsychicken.core.navigation.GameMode
import com.example.clumsychicken.domain.model.GameConfig
import javax.inject.Inject

class StartGameUseCase @Inject constructor() {

    operator fun invoke(mode: GameMode): GameConfig {
        return when (mode) {
            GameMode.EASY -> GameConfig(
                mode = mode,
                maxItems = 3,
                timeSeconds = 30,
                radiusRange = 48f..72f,
                speedRange = 180f..260f,
                spawnMs = 700,
                badChance = 0.0f
            )
            GameMode.HARD -> GameConfig(
                mode = mode,
                maxItems = 6,
                timeSeconds = 25,
                radiusRange = 40f..60f,
                speedRange = 300f..420f,
                spawnMs = 400,
                badChance = 0.15f
            )
        }
    }
}

