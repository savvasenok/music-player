package xyz.savvamirzoyan.musicplayer.feature_songs_list

import xyz.savvamirzoyan.musicplayer.appcore.SongUi
import xyz.savvamirzoyan.musicplayer.appcore.uistate.TextValue
import xyz.savvamirzoyan.musicplayer.core.StringID
import xyz.savvamirzoyan.musicplayer.core.mapper.Mapper
import xyz.savvamirzoyan.musicplayer.usecase_core.model.SongDomain
import javax.inject.Inject
import javax.inject.Singleton

interface SongDomainToUiMapper : Mapper {

    fun map(
        model: SongDomain,
        currentSongPlayingId: StringID?,
        isPlaying: Boolean,
        callback: (songId: StringID) -> Unit
    ): SongUi

    @Singleton
    class Base @Inject constructor() : SongDomainToUiMapper {

        override fun map(
            model: SongDomain,
            currentSongPlayingId: StringID?,
            isPlaying: Boolean,
            callback: (songId: StringID) -> Unit
        ) = SongUi(
            id = model.id,
            albumId = model.albumId,
            title = TextValue(model.title),
            artist = TextValue(model.artist),
            albumPictureUrl = model.albumPictureUrl,
            isExplicit = model.isExplicit,
            onClickListener = callback,
            isPlaying = model.id == currentSongPlayingId && isPlaying
        )
    }
}
