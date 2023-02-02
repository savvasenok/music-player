package xyz.savvamirzoyan.musicplayer.feature_playlist

import xyz.savvamirzoyan.musicplayer.appcore.TextValue
import xyz.savvamirzoyan.musicplayer.core.Model
import xyz.savvamirzoyan.musicplayer.core.PictureUrl

data class PlaylistInfoUi(
    val coverUrl: PictureUrl,
    val albumTitle: TextValue,
    val albumDescription: TextValue,
    val authorPictureUrl: PictureUrl,
    val authorName: TextValue,
    val briefInformation: TextValue
) : Model.Ui