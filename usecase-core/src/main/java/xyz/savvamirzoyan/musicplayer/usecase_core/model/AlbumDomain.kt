package xyz.savvamirzoyan.musicplayer.usecase_core.model

import xyz.savvamirzoyan.musicplayer.core.ID
import xyz.savvamirzoyan.musicplayer.core.Model

data class AlbumDomain(
    val id: ID
) : Model.Domain