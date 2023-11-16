package com.lessgenius.maengland.di

import android.content.Context
import com.lessgenius.maengland.data.datasource.local.PreferencesManager
import com.lessgenius.maengland.data.datasource.remote.AccountService
import com.lessgenius.maengland.data.datasource.remote.GameService
import com.lessgenius.maengland.data.repository.AccountRepository
import com.lessgenius.maengland.data.repository.AccountRepositoryImpl
import com.lessgenius.maengland.data.repository.GameRepository
import com.lessgenius.maengland.data.repository.GameRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RepositoryModule {

    @Provides
    @Singleton
    fun providePreferenceDataSource(@ApplicationContext context: Context): PreferencesManager {
        return PreferencesManager(context)
    }

    @Provides
    @Singleton
    fun provideAccountRepository(
        accountService: AccountService,
        dataStore: PreferencesManager
    ): AccountRepository {
        return AccountRepositoryImpl(accountService, dataStore)
    }

    @Provides
    @Singleton
    fun provideGameRepository(
        gameService: GameService
    ): GameRepository {
        return GameRepositoryImpl(gameService)
    }

}