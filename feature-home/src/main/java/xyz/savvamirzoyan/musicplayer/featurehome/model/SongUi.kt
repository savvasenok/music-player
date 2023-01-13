package xyz.savvamirzoyan.musicplayer.featurehome.model

import xyz.savvamirzoyan.musicplayer.appcore.TextValue
import xyz.savvamirzoyan.musicplayer.core.Model
import xyz.savvamirzoyan.musicplayer.core.PictureUrl
import xyz.savvamirzoyan.musicplayer.core.StringID

class LastPlayedSongsStateUi(_songs: List<SongUi>) : Model.Ui {
    val songs = _songs.take(5)
}

data class SongUi(
    val id: StringID,
    val albumId: StringID,
    val title: TextValue,
    val artist: TextValue,
    val albumPictureUrl: PictureUrl,
    val isExplicit: Boolean,
    val onClickListener: (songId: StringID, albumId: StringID) -> Unit
) : Model.Ui