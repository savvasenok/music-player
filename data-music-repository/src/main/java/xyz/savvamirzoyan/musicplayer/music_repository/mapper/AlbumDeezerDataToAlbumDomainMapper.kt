package xyz.savvamirzoyan.musicplayer.music_repository.mapper

import xyz.savvamirzoyan.musicplayer.core.mapper.Mapper
import xyz.savvamirzoyan.musicplayer.music_repository.model.deezer.AlbumDeezerData
import xyz.savvamirzoyan.musicplayer.usecase_core.model.SongCompilationDomain
import javax.inject.Inject

interface AlbumDeezerDataToAlbumDomainMapper : Mapper {

    fun map(model: AlbumDeezerData): SongCompilationDomain.AlbumDomain

    class Base @Inject constructor(
        private val songDeezerDomainToSongDomainMapper: SongDeezerDataToSongDomainMapper
    ) : AlbumDeezerDataToAlbumDomainMapper {

        override fun map(model: AlbumDeezerData) = SongCompilationDomain.AlbumDomain(
            id = model.id.toString(),
            title = model.title,
            authorName = model.artist?.name ?: "NO DATA",
            authorPictureUrl = model.artist?.pictureXl,
            description = null,
            songs = model.tracks?.data?.map { songDeezerDomainToSongDomainMapper.map(it) } ?: emptyList(),
            coverPictureUrl = model.coverXl,
            fans = model.fans ?: 0
        )
    }
}