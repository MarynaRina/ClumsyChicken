package com.example.clumsychicken.di

import android.content.Context
import com.example.clumsychicken.data.prefs.HighScoreDataSource
import com.example.clumsychicken.data.repository.HighScoreRepositoryImpl
import com.example.clumsychicken.domain.repository.HighScoreRepository
import com.example.clumsychicken.domain.usecase.ObserveHighScoreUseCase
import com.example.clumsychicken.domain.usecase.UpdateHighScoreUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides @Singleton
    fun provideHighScoreDataSource(@ApplicationContext context: Context) = HighScoreDataSource(context)

    @Provides @Singleton
    fun provideHighScoreRepository(ds: HighScoreDataSource): HighScoreRepository = HighScoreRepositoryImpl(ds)

    @Provides @Singleton
    fun provideObserveHighScore(repo: HighScoreRepository) = ObserveHighScoreUseCase(repo)

    @Provides @Singleton
    fun provideUpdateHighScore(repo: HighScoreRepository) = UpdateHighScoreUseCase(repo)
}