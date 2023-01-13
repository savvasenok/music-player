package xyz.savvamirzoyan.musicplayer.featurehome

import xyz.savvamirzoyan.musicplayer.appcore.TextValue
import xyz.savvamirzoyan.musicplayer.core.StringID
import xyz.savvamirzoyan.musicplayer.core.mapper.Mapper
import xyz.savvamirzoyan.musicplayer.featurehome.model.SongUi
import xyz.savvamirzoyan.musicplayer.usecase_core.model.SongDomain
import javax.inject.Inject
import javax.inject.Singleton

interface SongDomainToUiMapper : Mapper {

    fun map(model: SongDomain, callback: (songId: StringID, albumId: StringID) -> Unit): SongUi

    @Singleton
    class Base @Inject constructor() : SongDomainToUiMapper {

        override fun map(model: SongDomain, callback: (songId: StringID, albumId: StringID) -> Unit) = SongUi(
            id = model.id,
            albumId = model.albumId,
            title = TextValue.AsString(model.title),
            artist = TextValue.AsString(model.artist),
            albumPictureUrl = model.albumPictureUrl,
            isExplicit = model.isExplicit,
            onClickListener = callback
        )
    }
}
