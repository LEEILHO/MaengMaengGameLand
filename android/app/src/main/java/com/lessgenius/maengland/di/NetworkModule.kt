package com.lessgenius.maengland.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.intuit.sdp.BuildConfig
import com.lessgenius.maengland.data.datasource.local.PreferencesManager
import com.lessgenius.maengland.data.datasource.remote.AccountService
import com.lessgenius.maengland.data.interceptor.RequestInterceptor
import com.lessgenius.maengland.data.interceptor.ResponseInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    const val BASE_URL = "https://maengland.com/api/v1/"

    @Singleton
    @Provides
    fun provideOkHttpClient(
        requestInterceptor: RequestInterceptor,
        responseInterceptor: ResponseInterceptor
    ): OkHttpClient =
        OkHttpClient.Builder()
            .readTimeout(5000, TimeUnit.MILLISECONDS)
            .connectTimeout(5000, TimeUnit.MILLISECONDS)
            .addInterceptor(requestInterceptor) // Refresh Token
            .addInterceptor(responseInterceptor) // JWT 자동 헤더 전송
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build()

    @Singleton
    @Provides
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        requestInterceptor: RequestInterceptor,
        responseInterceptor: ResponseInterceptor
    ): Retrofit {
        val gson: Gson = GsonBuilder()
            .setLenient()
            .create()
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient)
            .build()
    }

    @Singleton
    @Provides
    fun provideAccountService(retrofit: Retrofit): AccountService =
        retrofit.create(AccountService::class.java)
}