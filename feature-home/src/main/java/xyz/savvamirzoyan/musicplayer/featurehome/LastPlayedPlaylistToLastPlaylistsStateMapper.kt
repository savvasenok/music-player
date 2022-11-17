package xyz.savvamirzoyan.musicplayer.featurehome

import xyz.savvamirzoyan.musicplayer.appcore.TextValue
import xyz.savvamirzoyan.musicplayer.featurehome.model.LastPlaylistStateUi
import xyz.savvamirzoyan.musicplayer.featurehome.model.LastPlaylistsStateUi
import xyz.savvamirzoyan.musicplayer.usecaseplayhistory.model.LastPlayedPlaylistDomain
import javax.inject.Inject

interface LastPlayedPlaylistToLastPlaylistsStateMapper {

    fun map(models: List<LastPlayedPlaylistDomain>): LastPlaylistsStateUi

    class Base @Inject constructor() : LastPlayedPlaylistToLastPlaylistsStateMapper {

        override fun map(models: List<LastPlayedPlaylistDomain>) = models
            .map {
                LastPlaylistStateUi(
                    title = TextValue.AsString(it.title),
                    pictureUrl = it.pictureUrl,
                    isPlaying = it.id == 0
                )
            }.let {
                LastPlaylistsStateUi(
                    it.getOrNull(0),
                    it.getOrNull(1),
                    it.getOrNull(2),
                    it.getOrNull(3),
                    it.getOrNull(4),
                    it.getOrNull(5),
                )
            }
    }
}
