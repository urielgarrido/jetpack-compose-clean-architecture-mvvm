package com.example.jetpackcompose_cleanarchitecture_mvvm.di

import android.app.Application
import androidx.room.Room
import com.example.jetpackcompose_cleanarchitecture_mvvm.BuildConfig
import com.example.jetpackcompose_cleanarchitecture_mvvm.data.local.dao.DrinkDao
import com.example.jetpackcompose_cleanarchitecture_mvvm.data.local.db.DRINK_DATABASE_NAME
import com.example.jetpackcompose_cleanarchitecture_mvvm.data.local.db.DrinkDatabase
import com.example.jetpackcompose_cleanarchitecture_mvvm.data.remote.api.DrinkApi
import com.example.jetpackcompose_cleanarchitecture_mvvm.data.repository.DrinkRepositoryImpl
import com.example.jetpackcompose_cleanarchitecture_mvvm.domain.repository.DrinkRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDrinkDatabase(app: Application): DrinkDatabase =
        Room.databaseBuilder(
            app,
            DrinkDatabase::class.java,
            DRINK_DATABASE_NAME
        ).build()

    @Provides
    @Singleton
    fun provideDrinkDao(drinkDatabase: DrinkDatabase) = drinkDatabase.drinkDao

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit =
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build())
            .build()

    @Provides
    @Singleton
    fun provideDrinkApi(retrofit: Retrofit): DrinkApi =
        retrofit.create(DrinkApi::class.java)

    @Provides
    @Singleton
    fun provideDrinkRepository(drinkApi: DrinkApi, drinkDao: DrinkDao): DrinkRepository =
        DrinkRepositoryImpl(drinkApi, drinkDao)

}