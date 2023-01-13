package xyz.savvamirzoyan.musicplayer.music_repository.mapper

import xyz.savvamirzoyan.musicplayer.core.mapper.Mapper
import xyz.savvamirzoyan.musicplayer.music_repository.model.deezer.AlbumDeezerData
import xyz.savvamirzoyan.musicplayer.usecase_core.model.AlbumDomain
import javax.inject.Inject

interface AlbumDeezerDataToAlbumDomainMapper : Mapper {

    fun map(model: AlbumDeezerData): AlbumDomain

    class Base @Inject constructor(
        private val songDeezerDomainToSongDomainMapper: SongDeezerDataToSongDomainMapper
    ) : AlbumDeezerDataToAlbumDomainMapper {

        override fun map(model: AlbumDeezerData) = AlbumDomain(
            id = model.id.toString(),
            title = model.title,
            songs = model.tracks?.data?.map { songDeezerDomainToSongDomainMapper.map(it) } ?: emptyList(),
            coverPictureUrl = model.coverXl
        )
    }
}