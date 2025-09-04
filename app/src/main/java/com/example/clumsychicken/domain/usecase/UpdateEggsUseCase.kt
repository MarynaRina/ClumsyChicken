package com.example.clumsychicken.domain.usecase

import com.example.clumsychicken.domain.model.FallingEgg
import javax.inject.Inject

class UpdateEggsUseCase @Inject constructor() {

    private val floorY = 0.88f
    private val frameTimeMs = 16
    private val brokenTtlMs = 2000

    operator fun invoke(
        eggs: List<FallingEgg>,
        screenHeight: Float
    ): List<FallingEgg> {
        return eggs.mapNotNull { egg ->
            when {
                egg.isBroken -> {
                    val ttl = egg.brokenTtlMs - frameTimeMs
                    if (ttl <= 0) null else egg.copy(brokenTtlMs = ttl)
                }
                else -> {
                    val newY = egg.y + (egg.speed / screenHeight) * (frameTimeMs / 1000f)
                    if (newY > floorY) {
                        egg.copy(isBroken = true, y = floorY, brokenTtlMs = brokenTtlMs)
                    } else {
                        egg.copy(y = newY)
                    }
                }
            }
        }
    }
}

