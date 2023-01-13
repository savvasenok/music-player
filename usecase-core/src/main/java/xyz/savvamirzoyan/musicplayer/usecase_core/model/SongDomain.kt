package xyz.savvamirzoyan.musicplayer.usecase_core.model

import xyz.savvamirzoyan.musicplayer.core.Model
import xyz.savvamirzoyan.musicplayer.core.PictureUrl
import xyz.savvamirzoyan.musicplayer.core.SongUrl
import xyz.savvamirzoyan.musicplayer.core.StringID

data class SongDomain(
    val id: StringID,
    val title: String,
    val artist: String,
    val albumPictureUrl: PictureUrl,
    val songUrl: SongUrl,
    val isExplicit: Boolean,
    val albumId: StringID
) : Model.Domain