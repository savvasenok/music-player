package xyz.savvamirzoyan.musicplayer.usecaseplayhistory

import xyz.savvamirzoyan.musicplayer.usecase_core.MusicRepository
import xyz.savvamirzoyan.musicplayer.usecase_core.model.SongCompilationDomain
import xyz.savvamirzoyan.musicplayer.usecase_core.model.SongDomain
import xyz.savvamirzoyan.musicplayer.usecaseplayhistory.model.LastPlayedPlaylistDomain
import javax.inject.Inject

interface PlayHistoryUseCase {

    suspend fun getLastPlayedSongs(): List<SongDomain>
    suspend fun getLastPlayedPlaylists(): List<LastPlayedPlaylistDomain>

    class Base @Inject constructor(
        private val musicRepository: MusicRepository
    ) : PlayHistoryUseCase {

        override suspend fun getLastPlayedSongs(): List<SongDomain> =
            musicRepository.getSongCompilation("9410086")!!.songs

        override suspend fun getLastPlayedPlaylists(): List<LastPlayedPlaylistDomain> = listOf(
            musicRepository.getSongCompilation("217281"),
            musicRepository.getSongCompilation("11205422"),
            musicRepository.getSongCompilation("1262261"),
            musicRepository.getSongCompilation("306788197"),
        ).map {

            val _album = it as? SongCompilationDomain.AlbumDomain
            _album?.let { album ->
                LastPlayedPlaylistDomain(
                    id = album.id,
                    title = album.title,
                    lastPlayedTimeUTC = 0,
                    pictureUrl = album.coverPictureUrl
                )
            }!!
        }
    }
}