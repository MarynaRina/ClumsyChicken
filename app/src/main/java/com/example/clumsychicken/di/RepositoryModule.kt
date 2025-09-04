package com.example.clumsychicken.di

import com.example.clumsychicken.data.repository.RandomRepositoryImpl
import com.example.clumsychicken.domain.repository.RandomRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindRandomRepository(
        randomRepositoryImpl: RandomRepositoryImpl
    ): RandomRepository
}

