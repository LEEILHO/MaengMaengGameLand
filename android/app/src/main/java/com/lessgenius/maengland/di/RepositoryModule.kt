package com.lessgenius.maengland.di

import com.lessgenius.maengland.data.datasource.remote.AccountService
import com.lessgenius.maengland.data.repository.AccountRepository
import com.lessgenius.maengland.data.repository.AccountRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RepositoryModule {

    @Provides
    @Singleton
    fun provideMainRepository(accountService: AccountService): AccountRepository {
        return AccountRepositoryImpl(accountService)
    }
}