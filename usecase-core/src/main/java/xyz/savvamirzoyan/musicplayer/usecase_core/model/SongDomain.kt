package xyz.savvamirzoyan.musicplayer.usecase_core.model

import xyz.savvamirzoyan.musicplayer.core.ID
import xyz.savvamirzoyan.musicplayer.core.Model
import xyz.savvamirzoyan.musicplayer.core.PictureUrl
import xyz.savvamirzoyan.musicplayer.core.SongUrl

data class SongDomain(
    val id: ID,
    val title: String,
    val artist: String,
    val albumPictureUrl: PictureUrl,
    val songUrl: SongUrl,
    val isExplicit: Boolean,
    val localCurrentPlaylistId: ID
) : Model.Domain