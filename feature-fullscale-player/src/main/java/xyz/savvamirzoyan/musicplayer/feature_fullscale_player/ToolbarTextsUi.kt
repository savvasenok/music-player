package xyz.savvamirzoyan.musicplayer.feature_fullscale_player

import xyz.savvamirzoyan.musicplayer.appcore.uistate.TextValue
import xyz.savvamirzoyan.musicplayer.core.Model

data class ToolbarTextsUi(
    val title: TextValue,
    val subtitle: TextValue
) : Model.Ui