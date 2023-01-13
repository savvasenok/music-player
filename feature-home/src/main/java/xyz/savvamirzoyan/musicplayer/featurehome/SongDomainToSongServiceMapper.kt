package xyz.savvamirzoyan.musicplayer.featurehome

import xyz.savvamirzoyan.featuremusicplayerservice.SongService
import xyz.savvamirzoyan.musicplayer.core.mapper.Mapper
import xyz.savvamirzoyan.musicplayer.usecase_core.model.SongDomain
import javax.inject.Inject
import javax.inject.Singleton

interface SongDomainToSongServiceMapper : Mapper {

    fun map(model: SongDomain): SongService

    @Singleton
    class Base @Inject constructor() : SongDomainToSongServiceMapper {

        override fun map(model: SongDomain) = SongService(
            mediaId = model.id,
            artist = model.artist,
            title = model.title,
            albumUrl = model.albumPictureUrl,
            songUrl = model.songUrl
        )
    }
}