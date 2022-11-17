package xyz.savvamirzoyan.musicplayer.featurehome

import xyz.savvamirzoyan.musicplayer.appcore.TextValue
import xyz.savvamirzoyan.musicplayer.core.mapper.Mapper
import xyz.savvamirzoyan.musicplayer.featurehome.model.LastPlayedSongUi
import xyz.savvamirzoyan.musicplayer.usecaseplayhistory.model.LastPlayedSongDomain
import javax.inject.Inject

interface LastPlayedSongDomainToUiMapper : Mapper {

    fun map(model: LastPlayedSongDomain): LastPlayedSongUi

    class Base @Inject constructor() : LastPlayedSongDomainToUiMapper {

        override fun map(model: LastPlayedSongDomain) = LastPlayedSongUi(
            id = model.id,
            title = TextValue.AsString(model.title),
            artist = TextValue.AsString(model.artist),
            albumPictureUrl = model.albumPictureUrl,
            isExplicit = model.isExplicit
        )
    }
}
