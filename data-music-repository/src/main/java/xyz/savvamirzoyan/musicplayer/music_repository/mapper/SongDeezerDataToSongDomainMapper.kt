package xyz.savvamirzoyan.musicplayer.music_repository.mapper

import xyz.savvamirzoyan.musicplayer.core.mapper.Mapper
import xyz.savvamirzoyan.musicplayer.music_repository.model.deezer.SongDeezerData
import xyz.savvamirzoyan.musicplayer.usecase_core.model.SongDomain
import javax.inject.Inject

interface SongDeezerDataToSongDomainMapper : Mapper {

    fun map(model: SongDeezerData, indexInCompilation: Int): SongDomain

    class Base @Inject constructor() : SongDeezerDataToSongDomainMapper {

        override fun map(model: SongDeezerData, indexInCompilation: Int) = SongDomain(
            id = model.id.toString(),
            indexInCompilation = indexInCompilation,
            title = model.title,
            artist = model.artist.name,
            albumPictureUrl = model.album.coverMedium,
            isExplicit = model.explicitLyrics,
            songUrl = model.preview,
            albumId = model.album.id.toString(),
            compilationId = model.album.id.toString() // TODO: now its just album, but also can be a playlist
        )
    }
}