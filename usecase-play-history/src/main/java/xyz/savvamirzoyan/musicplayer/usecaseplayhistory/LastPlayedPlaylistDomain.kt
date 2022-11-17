package xyz.savvamirzoyan.musicplayer.usecaseplayhistory

import xyz.savvamirzoyan.musicplayer.core.ID
import xyz.savvamirzoyan.musicplayer.core.Model
import xyz.savvamirzoyan.musicplayer.core.PictureUrl
import xyz.savvamirzoyan.musicplayer.core.UTC

data class LastPlayedPlaylistDomain(
    val id: ID,
    val title: String,
    val pictureUrl: PictureUrl?,
    val lastPlayedTimeUTC: UTC
) : Model.Domain
