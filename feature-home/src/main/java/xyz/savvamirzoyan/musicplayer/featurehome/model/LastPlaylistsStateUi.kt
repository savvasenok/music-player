package xyz.savvamirzoyan.musicplayer.featurehome.model

import xyz.savvamirzoyan.musicplayer.appcore.uistate.TextValue
import xyz.savvamirzoyan.musicplayer.core.Model
import xyz.savvamirzoyan.musicplayer.core.PictureUrl

data class LastPlaylistsStateUi(
    val first: LastPlaylistStateUi? = null,
    val second: LastPlaylistStateUi? = null,
    val third: LastPlaylistStateUi? = null,
    val fourth: LastPlaylistStateUi? = null,
    val fifth: LastPlaylistStateUi? = null,
    val sixth: LastPlaylistStateUi? = null
) : Model.Ui {

    val isFirstVisible = first != null
    val isSecondVisible = second != null
    val isThirdVisible = third != null
    val isFourthVisible = fourth != null
    val isFifthVisible = fifth != null
    val isSixthVisible = sixth != null
}

data class LastPlaylistStateUi(
    val pictureUrl: PictureUrl?,
    val title: TextValue,
    val isPlaying: Boolean
) : Model.Ui