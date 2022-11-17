package xyz.savvamirzoyan.musicplayer.featurehome.model

import xyz.savvamirzoyan.musicplayer.core.Model

internal data class ToolbarChipsStateUi(
    val isCancelChipVisible: Boolean,
    val isMusicChipVisible: Boolean,
    val isMusicSelected: Boolean,
    val isPodcastsAndShowsChipVisible: Boolean,
    val isPodcastsAndShowsChipSelected: Boolean
) : Model.Ui