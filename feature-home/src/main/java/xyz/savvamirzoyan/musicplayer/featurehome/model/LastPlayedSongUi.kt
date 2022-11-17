package xyz.savvamirzoyan.musicplayer.featurehome.model

import xyz.savvamirzoyan.musicplayer.appcore.TextValue
import xyz.savvamirzoyan.musicplayer.core.ID
import xyz.savvamirzoyan.musicplayer.core.Model
import xyz.savvamirzoyan.musicplayer.core.PictureUrl

class LastPlayedSongsStateUi(
    _songs: List<LastPlayedSongUi>
) : Model.Ui {
    val songs = _songs.take(5)
}

data class LastPlayedSongUi(
    val id: ID,
    val title: TextValue,
    val artist: TextValue,
    val albumPictureUrl: PictureUrl,
    val isExplicit: Boolean
) : Model.Ui