package xyz.savvamirzoyan.musicplayer.feature_songs_list

import xyz.savvamirzoyan.musicplayer.appcore.TextValue
import xyz.savvamirzoyan.musicplayer.core.Model
import xyz.savvamirzoyan.musicplayer.core.PictureUrl
import xyz.savvamirzoyan.musicplayer.core.StringID

data class SongUi(
    val id: StringID,
    val albumId: StringID,
    val title: TextValue,
    val artist: TextValue,
    val albumPictureUrl: PictureUrl,
    val isExplicit: Boolean,
    val onClickListener: (songId: StringID, albumId: StringID) -> Unit
) : Model.Ui