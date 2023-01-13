package xyz.savvamirzoyan.musicplayer.usecase_core.model

import xyz.savvamirzoyan.musicplayer.core.Model
import xyz.savvamirzoyan.musicplayer.core.PictureUrl
import xyz.savvamirzoyan.musicplayer.core.StringID

data class AlbumDomain(
    val id: StringID,
    val title: String,
    val coverPictureUrl: PictureUrl,
    val songs: List<SongDomain>
) : Model.Domain