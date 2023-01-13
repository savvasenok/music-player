package xyz.savvamirzoyan.musicplayer.music_repository

import xyz.savvamirzoyan.musicplayer.core.StringID
import xyz.savvamirzoyan.musicplayer.music_repository.api.DeezerApiService
import xyz.savvamirzoyan.musicplayer.music_repository.mapper.AlbumDeezerDataToAlbumDomainMapper
import xyz.savvamirzoyan.musicplayer.music_repository.mapper.SongDeezerDataToSongDomainMapper
import xyz.savvamirzoyan.musicplayer.usecase_core.model.AlbumDomain
import xyz.savvamirzoyan.musicplayer.usecase_core.model.SongDomain
import javax.inject.Inject

interface DeezerRepository {

    suspend fun getSong(songId: StringID): SongDomain?
    suspend fun getAlbum(albumId: StringID): AlbumDomain?

    class Base @Inject constructor(
        private val deezerApiService: DeezerApiService,
        private val songDeezerDataToSongDomainMapper: SongDeezerDataToSongDomainMapper,
        private val albumDeezerDataToAlbumDomainMapper: AlbumDeezerDataToAlbumDomainMapper
    ) : DeezerRepository {

        override suspend fun getSong(songId: StringID): SongDomain? = deezerApiService
            .getSong(songId)
            ?.let { songDeezerDataToSongDomainMapper.map(it) }

        override suspend fun getAlbum(albumId: StringID): AlbumDomain? = deezerApiService
            .getAlbum(albumId)
            ?.let { albumDeezerDataToAlbumDomainMapper.map(it) }

    }
}