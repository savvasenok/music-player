package xyz.savvamirzoyan.musicplayer.usecase_core

import xyz.savvamirzoyan.musicplayer.core.StringID
import xyz.savvamirzoyan.musicplayer.usecase_core.model.AlbumDomain
import xyz.savvamirzoyan.musicplayer.usecase_core.model.SongDomain

interface MusicRepository {

    suspend fun getSong(songId: StringID): SongDomain?
    suspend fun getAlbum(albumId: StringID): AlbumDomain
}
