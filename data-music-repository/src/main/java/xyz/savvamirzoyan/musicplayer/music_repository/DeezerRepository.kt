package xyz.savvamirzoyan.musicplayer.music_repository

import xyz.savvamirzoyan.musicplayer.core.ID
import xyz.savvamirzoyan.musicplayer.music_repository.api.DeezerApiService
import xyz.savvamirzoyan.musicplayer.music_repository.mapper.SongDeezerDataToSongDomainMapper
import xyz.savvamirzoyan.musicplayer.usecase_core.model.SongDomain
import javax.inject.Inject

interface DeezerRepository {

    suspend fun getSong(songId: ID): SongDomain?
    suspend fun getPlaylist(playlistId: ID): List<SongDomain>

    class Base @Inject constructor(
        private val deezerApiService: DeezerApiService,
        private val songDeezerDataToSongDomainMapper: SongDeezerDataToSongDomainMapper
    ) : DeezerRepository {

        override suspend fun getSong(songId: ID): SongDomain? = deezerApiService
            .getSong(songId)
            ?.let { songDeezerDataToSongDomainMapper.map(it) }

        override suspend fun getPlaylist(playlistId: ID): List<SongDomain> {
            TODO("Not yet implemented")
        }

        //        override suspend fun getPlaylist(playlistId: ID): List<SongDomain> = deezerApiService.
    }
}