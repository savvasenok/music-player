package xyz.savvamirzoyan.musicplayer.usecaseplayhistory.model

import xyz.savvamirzoyan.musicplayer.core.ID
import xyz.savvamirzoyan.musicplayer.core.Model
import xyz.savvamirzoyan.musicplayer.core.PictureUrl

data class LastPlayedSongDomain(
    val id: ID,
    val title: String,
    val artist: String,
    val albumPictureUrl: PictureUrl,
    val isExplicit: Boolean
) : Model.Domain
