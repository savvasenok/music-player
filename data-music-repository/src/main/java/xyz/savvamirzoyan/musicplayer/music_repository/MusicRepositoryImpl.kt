package xyz.savvamirzoyan.musicplayer.music_repository

import xyz.savvamirzoyan.musicplayer.core.StringID
import xyz.savvamirzoyan.musicplayer.usecase_core.MusicRepository
import xyz.savvamirzoyan.musicplayer.usecase_core.model.AlbumDomain
import xyz.savvamirzoyan.musicplayer.usecase_core.model.SongDomain
import javax.inject.Inject

class MusicRepositoryImpl @Inject constructor(
    private val deezerRepository: DeezerRepository
) : MusicRepository {

    override suspend fun getSong(songId: StringID): SongDomain {
        val song = deezerRepository.getSong(songId)
        return song ?: throw IllegalArgumentException("Song (id#$songId) does not exist in Deezer")
    }

    override suspend fun getAlbum(albumId: StringID): AlbumDomain = deezerRepository.getAlbum(albumId)
        ?: throw IllegalArgumentException("Album (id#$albumId) does not exist in Deezer")
}