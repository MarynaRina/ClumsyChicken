package com.example.clumsychicken.domain.usecase

import com.example.clumsychicken.domain.model.FallingEgg
import com.example.clumsychicken.domain.model.GameConfig
import com.example.clumsychicken.domain.repository.RandomRepository
import javax.inject.Inject

class SpawnEggUseCase @Inject constructor(
    private val randomRepository: RandomRepository
) {

    operator fun invoke(config: GameConfig): FallingEgg {
        val radius = randomRepository.nextFloat(config.radiusRange)
        val speed = randomRepository.nextFloat(config.speedRange)
        val isBad = randomRepository.nextFloat() < config.badChance
        val x = randomRepository.nextFloat(0.05f, 0.95f)

        return FallingEgg(
            id = randomRepository.nextLong(),
            x = x,
            y = 0f,
            speed = speed,
            radius = radius,
            isBad = isBad
        )
    }
}

