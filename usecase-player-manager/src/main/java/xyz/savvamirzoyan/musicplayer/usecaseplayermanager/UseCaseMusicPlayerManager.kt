package xyz.savvamirzoyan.musicplayer.usecaseplayermanager

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.zip
import xyz.savvamirzoyan.musicplayer.core.ID
import xyz.savvamirzoyan.musicplayer.usecase_core.model.SongDomain
import javax.inject.Inject

interface UseCaseMusicPlayerManager {

    suspend fun getSong(songId: String): SongDomain
//    suspend fun getSong(songId: ID): SongDomain
//    suspend fun getCurrentPlaylist(): List<SongDomain>
//    suspend fun getCurrentSongIndex(): Int

    val songsPlaylistFlow: Flow<List<SongDomain>>
    val currentSongIndexFlow: Flow<Int>
    val currentSongFlow: Flow<SongDomain>

    suspend fun getSongs(playFromSongId: String, fromPlaylistId: ID): List<SongDomain>
    suspend fun getSongs(playFromSongId: ID, fromPlaylistId: ID): List<SongDomain>

    class Base @Inject constructor(
        private val musicRepository: MusicRepository
    ) : UseCaseMusicPlayerManager {

        private val _songsPlaylistFlow = MutableSharedFlow<List<SongDomain>>(replay = 1)
        override val songsPlaylistFlow: Flow<List<SongDomain>> = _songsPlaylistFlow

        private val _currentSongIndexFlow = MutableSharedFlow<Int>(replay = 1)
        override val currentSongIndexFlow: Flow<Int> = _currentSongIndexFlow

        override val currentSongFlow: Flow<SongDomain> =
            songsPlaylistFlow.zip(currentSongIndexFlow) { playlist, index -> playlist[index] }

        override suspend fun getSongs(playFromSongId: String, fromPlaylistId: ID) =
            getSongs(playFromSongId.toInt(), fromPlaylistId)

        override suspend fun getSongs(playFromSongId: ID, fromPlaylistId: ID): List<SongDomain> = musicRepository
            .getPlaylist(playFromSongId)
            .also { playlist -> _songsPlaylistFlow.emit(playlist) }
            .also { playlist ->
                playlist
                    .find { song -> song.id == playFromSongId }
                    .also { song -> _currentSongIndexFlow.emit(playlist.indexOf(song)) }
            }

        override suspend fun getSong(songId: String): SongDomain = musicRepository.getSong(songId.toInt()) ?: throw IllegalArgumentException()

    }
}