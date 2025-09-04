package com.example.clumsychicken.data.repository

import com.example.clumsychicken.domain.repository.RandomRepository
import javax.inject.Inject
import kotlin.random.Random

class RandomRepositoryImpl @Inject constructor() : RandomRepository {

    private val random = Random(System.nanoTime())

    override fun nextFloat(): Float = random.nextFloat()

    override fun nextFloat(range: ClosedFloatingPointRange<Float>): Float {
        return range.start + random.nextFloat() * (range.endInclusive - range.start)
    }

    override fun nextFloat(min: Float, max: Float): Float {
        return min + random.nextFloat() * (max - min)
    }

    override fun nextLong(): Long = System.nanoTime()
}

