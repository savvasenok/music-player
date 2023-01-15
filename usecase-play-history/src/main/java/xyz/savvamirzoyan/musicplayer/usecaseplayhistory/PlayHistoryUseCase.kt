package xyz.savvamirzoyan.musicplayer.usecaseplayhistory

import xyz.savvamirzoyan.musicplayer.usecase_core.MusicRepository
import xyz.savvamirzoyan.musicplayer.usecase_core.model.SongDomain
import xyz.savvamirzoyan.musicplayer.usecaseplayhistory.model.LastPlayedPlaylistDomain
import javax.inject.Inject

interface PlayHistoryUseCase {

    suspend fun getLastPlayedSongs(): List<SongDomain>
    suspend fun getLastPlayedPlaylists(): List<LastPlayedPlaylistDomain>

    class Base @Inject constructor(
        private val musicRepository: MusicRepository
    ) : PlayHistoryUseCase {

        override suspend fun getLastPlayedSongs(): List<SongDomain> = musicRepository.getAlbum("9410086").songs
        override suspend fun getLastPlayedPlaylists(): List<LastPlayedPlaylistDomain> = listOf(
            musicRepository.getAlbum("217281"),
            musicRepository.getAlbum("11205422"),
            musicRepository.getAlbum("1262261"),
        ).map {
            LastPlayedPlaylistDomain(
                id = it.id,
                title = it.title,
                lastPlayedTimeUTC = 0,
                pictureUrl = it.coverPictureUrl
            )
        }
    }
}