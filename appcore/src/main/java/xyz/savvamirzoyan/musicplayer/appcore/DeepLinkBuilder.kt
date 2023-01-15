package xyz.savvamirzoyan.musicplayer.appcore

import xyz.savvamirzoyan.musicplayer.core.StringID
import javax.inject.Inject

class DeepLinkBuilder @Inject constructor() {

    fun buildPlaylistDeepLink(playlistId: StringID): String =
        "savvasenok-player://playlist?playlist_id=$playlistId"
}