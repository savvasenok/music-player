package xyz.savvamirzoyan.musicplayer.appcore

import xyz.savvamirzoyan.musicplayer.core.StringID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeepLinkBuilder @Inject constructor() {

    val fullscalePlayerDeepLink = "savvasenok-player://fullscale-player"

    fun buildPlaylistDeepLink(playlistId: StringID): String =
        "savvasenok-player://compilation?compilation_id=$playlistId"
}