package xyz.savvamirzoyan.musicplayer.featurehome.model

import xyz.savvamirzoyan.musicplayer.appcore.TextValue
import xyz.savvamirzoyan.musicplayer.core.ID
import xyz.savvamirzoyan.musicplayer.core.Model
import xyz.savvamirzoyan.musicplayer.core.PictureUrl

class LastPlayedSongsStateUi(
    _songs: List<SongUi>
) : Model.Ui {
    val songs = _songs.take(5)
}

data class SongUi(
    val id: ID,
    val title: TextValue,
    val artist: TextValue,
    val albumPictureUrl: PictureUrl,
    val isExplicit: Boolean,
    val onClickListener: (songId: ID) -> Unit
) : Model.Ui