package xyz.savvamirzoyan.musicplayer.music_repository

import xyz.savvamirzoyan.musicplayer.core.ID
import xyz.savvamirzoyan.musicplayer.usecase_core.model.SongDomain
import xyz.savvamirzoyan.musicplayer.usecaseplayermanager.MusicRepository
import javax.inject.Inject

class MusicRepositoryImpl @Inject constructor(
    private val deezerRepository: DeezerRepository
) : MusicRepository {

    override suspend fun getSong(songId: ID): SongDomain {
        val song = deezerRepository.getSong(songId)
        return song ?: throw IllegalArgumentException("Song (id#$songId) does not exist in Deezer")
    }

    override suspend fun getPlaylist(playlistId: ID): List<SongDomain> = emptyList()
}