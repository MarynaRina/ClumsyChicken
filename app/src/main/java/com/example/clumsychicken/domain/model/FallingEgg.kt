package com.example.clumsychicken.domain.model

data class FallingEgg(
    val id: Long,
    val x: Float,
    val y: Float,
    val speed: Float,
    val radius: Float,
    val isBroken: Boolean = false,
    val brokenTtlMs: Int = 0,
    val isBad: Boolean = false
)