package com.dkmarkell.rakutentakehome.di

import android.content.Context
import androidx.room.Room
import androidx.viewbinding.BuildConfig
import com.dkmarkell.rakutentakehome.localstorage.LocalDatabase
import com.dkmarkell.rakutentakehome.photo.PhotoApi
import com.dkmarkell.rakutentakehome.photo.PhotoDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideBaseUrl() = "http://yuriy.me"

    @Singleton
    @Provides
    fun provideOkHttpClient() = if (BuildConfig.DEBUG) {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    } else {
        OkHttpClient
            .Builder()
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient, BASE_URL: String): Retrofit = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create())
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .build()

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): PhotoApi = retrofit.create(PhotoApi::class.java)

    @Singleton
    @Provides
    fun provideBudgetTrackerDatabase(@ApplicationContext appContext: Context): LocalDatabase {

        return Room.databaseBuilder(
            appContext.applicationContext,
            LocalDatabase::class.java,
            "rakuten_take_home_database"
        ).build()
    }

    @Singleton
    @Provides
    fun providePhotoDao(database: LocalDatabase): PhotoDao {
        return database.photoDao
    }
}