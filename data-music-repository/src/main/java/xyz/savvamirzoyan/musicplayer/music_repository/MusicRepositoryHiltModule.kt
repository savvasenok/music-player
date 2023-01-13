@file:OptIn(ExperimentalSerializationApi::class)

package xyz.savvamirzoyan.musicplayer.music_repository

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import xyz.savvamirzoyan.musicplayer.music_repository.api.DeezerApiService
import xyz.savvamirzoyan.musicplayer.music_repository.mapper.SongDeezerDataToSongDomainMapper
import xyz.savvamirzoyan.musicplayer.usecaseplayermanager.MusicRepository
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
interface MusicRepositoryHiltModule {

    @Singleton
    @Binds
    fun bindMusicRepository(impl: MusicRepositoryImpl): MusicRepository

    @Singleton
    @Binds
    fun bindDeezerRepository(base: DeezerRepository.Base): DeezerRepository

    @Singleton
    @Binds
    fun bindSongDeezerDataToSongDomainMapper(base: SongDeezerDataToSongDomainMapper.Base): SongDeezerDataToSongDomainMapper
}

@InstallIn(SingletonComponent::class)
@Module
object MusicRepositoryObjectHiltModule {

    private val json = Json { ignoreUnknownKeys = true }
    private val contentType = "application/json".toMediaType()

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })
        .build()

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl(Constants.DEEZER_API)
        .addConverterFactory(json.asConverterFactory(contentType))
        .build()

    @Singleton
    @Provides
    fun bindDeezerApiService(retrofit: Retrofit): DeezerApiService =
        retrofit.create(DeezerApiService::class.java)
}