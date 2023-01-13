@file:OptIn(ExperimentalSerializationApi::class)
@file:Suppress("unused")

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
import xyz.savvamirzoyan.musicplayer.music_repository.mapper.AlbumDeezerDataToAlbumDomainMapper
import xyz.savvamirzoyan.musicplayer.music_repository.mapper.SongDeezerDataToSongDomainMapper
import xyz.savvamirzoyan.musicplayer.usecase_core.MusicRepository
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class MusicRepositoryHiltModule {

    @Singleton
    @Binds
    abstract fun bindMusicRepository(impl: MusicRepositoryImpl): MusicRepository

    @Singleton
    @Binds
    abstract fun bindDeezerRepository(base: DeezerRepository.Base): DeezerRepository

    @Singleton
    @Binds
    abstract fun bindSongDeezerDataToSongDomainMapper(base: SongDeezerDataToSongDomainMapper.Base): SongDeezerDataToSongDomainMapper

    @Singleton
    @Binds
    abstract fun bindAlbumDeezerDataToAlbumDomainMapper(base: AlbumDeezerDataToAlbumDomainMapper.Base): AlbumDeezerDataToAlbumDomainMapper

    companion object {

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
}