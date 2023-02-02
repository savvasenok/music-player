package xyz.savvamirzoyan.musicplayer.appcore

import xyz.savvamirzoyan.musicplayer.core.Model

data class SongService(
    val mediaId: String,
    val artist: String,
    val title: String,
    val albumUrl: String,
    val songUrl: String,
) : Model.Ui