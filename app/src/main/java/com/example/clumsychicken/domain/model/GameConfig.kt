package com.example.clumsychicken.domain.model

import com.example.clumsychicken.core.navigation.GameMode

data class GameConfig(
    val mode: GameMode,
    val maxItems: Int,
    val timeSeconds: Int,
    val radiusRange: ClosedFloatingPointRange<Float>,
    val speedRange: ClosedFloatingPointRange<Float>,
    val spawnMs: Int,
    val badChance: Float
)