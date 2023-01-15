package xyz.savvamirzoyan.musicplayer.usecaseplayhistory.model

import xyz.savvamirzoyan.musicplayer.core.Model
import xyz.savvamirzoyan.musicplayer.core.PictureUrl
import xyz.savvamirzoyan.musicplayer.core.StringID
import xyz.savvamirzoyan.musicplayer.core.UTC

data class LastPlayedPlaylistDomain(
    val id: StringID,
    val title: String,
    val pictureUrl: PictureUrl,
    val lastPlayedTimeUTC: UTC
) : Model.Domain
