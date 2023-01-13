package xyz.savvamirzoyan.musicplayer.usecaseplayermanager

import kotlinx.coroutines.flow.*
import xyz.savvamirzoyan.musicplayer.core.StringID
import xyz.savvamirzoyan.musicplayer.usecase_core.MusicRepository
import xyz.savvamirzoyan.musicplayer.usecase_core.model.SongDomain
import javax.inject.Inject

interface UseCaseMusicPlayerManager {

    var currentSongsPlaylist: List<SongDomain>

    val songsPlaylistFlow: Flow<List<SongDomain>>
    val currentSongIndexFlow: Flow<Int>
    val currentSongFlow: Flow<SongDomain>

    suspend fun getSong(songId: StringID): SongDomain
    suspend fun getSongs(playFromSongId: String, fromPlaylistId: StringID): List<SongDomain>
    suspend fun getCurrentPlaylistFromMediaId(mediaId: String): List<SongDomain>

    class Base @Inject constructor(
        private val musicRepository: MusicRepository
    ) : UseCaseMusicPlayerManager {

        override var currentSongsPlaylist: List<SongDomain> = emptyList()

        private val _songsPlaylistFlow = MutableSharedFlow<List<SongDomain>>(replay = 1)
        override val songsPlaylistFlow: Flow<List<SongDomain>> = _songsPlaylistFlow
            .onEach { currentSongsPlaylist = it }

        private val _currentSongIndexFlow = MutableSharedFlow<Int>(replay = 1)
        override val currentSongIndexFlow: Flow<Int> = _currentSongIndexFlow

        override val currentSongFlow: Flow<SongDomain> =
            songsPlaylistFlow.zip(currentSongIndexFlow) { playlist, index -> playlist[index] }

        override suspend fun getSongs(playFromSongId: StringID, fromPlaylistId: StringID): List<SongDomain> =
            musicRepository
                .getAlbum(fromPlaylistId).songs
                .let { songs ->
                    songs
                        .find { song -> song.id == playFromSongId }
                        .let { song -> songs.indexOf(song) }
                        .let { songIndex -> songs.subList(songIndex, songs.lastIndex) }
                }
                .also { songs -> _songsPlaylistFlow.emit(songs) }

        override suspend fun getSong(songId: StringID): SongDomain =
            musicRepository.getSong(songId) ?: throw IllegalArgumentException()

        override suspend fun getCurrentPlaylistFromMediaId(mediaId: String): List<SongDomain> =
            songsPlaylistFlow.firstOrNull() ?: emptyList()
    }
}