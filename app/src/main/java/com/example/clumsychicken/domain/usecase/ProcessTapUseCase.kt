package com.example.clumsychicken.domain.usecase

import com.example.clumsychicken.domain.model.FallingEgg
import javax.inject.Inject
import kotlin.math.hypot

class ProcessTapUseCase @Inject constructor() {

    data class TapResult(
        val hitEgg: FallingEgg?,
        val newScore: Int,
        val remainingEggs: List<FallingEgg>
    )

    operator fun invoke(
        x: Float,
        y: Float,
        eggs: List<FallingEgg>,
        currentScore: Int,
        screenWidth: Float,
        screenHeight: Float
    ): TapResult {
        val hitEgg = eggs.firstOrNull { egg ->
            val dx = x - egg.x * screenWidth
            val dy = y - egg.y * screenHeight
            hypot(dx, dy) <= egg.radius && !egg.isBroken
        }

        return if (hitEgg != null) {
            val newScore = if (hitEgg.isBad) {
                maxOf(0, currentScore - 2)
            } else {
                currentScore + 1
            }
            val remainingEggs = eggs.filter { it.id != hitEgg.id }
            TapResult(hitEgg, newScore, remainingEggs)
        } else {
            TapResult(null, currentScore, eggs)
        }
    }
}

