package xyz.savvamirzoyan.musicplayer.usecaseplayermanager

import xyz.savvamirzoyan.musicplayer.core.ID
import xyz.savvamirzoyan.musicplayer.usecase_core.model.SongDomain

interface MusicRepository {

    suspend fun getSong(songId: ID): SongDomain?

    suspend fun getPlaylist(playlistId: ID): List<SongDomain>
}
