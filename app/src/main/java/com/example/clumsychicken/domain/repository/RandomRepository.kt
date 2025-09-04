package com.example.clumsychicken.domain.repository

interface RandomRepository {
    fun nextFloat(): Float
    fun nextFloat(range: ClosedFloatingPointRange<Float>): Float
    fun nextFloat(min: Float, max: Float): Float
    fun nextLong(): Long
}

