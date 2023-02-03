package xyz.savvamirzoyan.musicplayer.usecaseplayermanager

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import xyz.savvamirzoyan.musicplayer.core.StringID
import xyz.savvamirzoyan.musicplayer.usecase_core.MusicRepository
import xyz.savvamirzoyan.musicplayer.usecase_core.model.SongCompilationDomain
import xyz.savvamirzoyan.musicplayer.usecase_core.model.SongDomain
import javax.inject.Inject

// core "brain" module to handle all logic of music playing
interface UseCaseMusicPlayerManager {

    val currentSongsCompilation: List<SongDomain>

    val currentSongFlow: Flow<SongDomain?>
    val currentUserSelectedSongFlow: Flow<SongDomain?>
    val currentCompilationFlow: Flow<SongCompilationDomain?>

    val isPlayingFlow: Flow<Boolean>

    suspend fun getSong(songId: StringID): SongDomain
    suspend fun getCompilation(compilationId: StringID): SongCompilationDomain

    suspend fun pause()
    suspend fun play()
    suspend fun play(songId: StringID)
    suspend fun playCompilation(compilationId: StringID)

    suspend fun updatePlayingCurrentSong(songId: StringID)
    suspend fun onSongPlaying()
    suspend fun onSongPaused()
    suspend fun onSongCompilationEnd()

    class Base @Inject constructor(private val musicRepository: MusicRepository) : UseCaseMusicPlayerManager {

        override var currentSongsCompilation: List<SongDomain> = mutableListOf()
            private set

        private val _isPlayingFlow = MutableStateFlow(false)
        override val isPlayingFlow: Flow<Boolean> = _isPlayingFlow

        private val _currentSongFlow = MutableStateFlow<SongDomain?>(null)
        override val currentSongFlow: Flow<SongDomain?> = _currentSongFlow

        private val _currentUserSelectedSongFlow = MutableSharedFlow<SongDomain>(replay = 0)
        override val currentUserSelectedSongFlow: Flow<SongDomain?> = _currentUserSelectedSongFlow

        private val _currentCompilationFlow = MutableStateFlow<SongCompilationDomain?>(null)
        override val currentCompilationFlow: Flow<SongCompilationDomain?> = _currentCompilationFlow

        override suspend fun getSong(songId: StringID): SongDomain =
            musicRepository.getSong(songId)
                ?: throw IllegalArgumentException("No song with this id ($songId)")

        override suspend fun getCompilation(compilationId: StringID): SongCompilationDomain =
            musicRepository.getSongCompilation(compilationId)
                ?: throw IllegalArgumentException("No song compilation with this id ($compilationId)")

        override suspend fun pause() {
            _isPlayingFlow.emit(false)
        }

        override suspend fun play() {
            _isPlayingFlow.emit(true)
        }

        override suspend fun play(songId: StringID) {

            val song = musicRepository.getSong(songId)
            val compilation = song?.let { musicRepository.getSongCompilation(song.compilationId) }

            currentSongsCompilation = compilation?.songs ?: emptyList()

            _currentSongFlow.emit(song)
            if (song != null) _currentUserSelectedSongFlow.emit(song)
            _currentCompilationFlow.emit(compilation)

            play()
        }

        override suspend fun playCompilation(compilationId: StringID) {
            val isPlaying = isPlayingFlow.first()
            val currentCompilation = currentCompilationFlow.first()

            // if user is trying to interact with the same album or playlist
            if (currentCompilation?.id == compilationId) {
                if (isPlaying) pause() else play()
            } else {
                musicRepository.getSongCompilation(compilationId)
                    ?.also { compilation -> play(compilation.songs.first().id) }
            }
        }

        // called from MusicServiceConnection. Updated, when song finishes and new one starts
        override suspend fun updatePlayingCurrentSong(songId: StringID) {
            currentSongsCompilation.find { it.id == songId }
                ?.also { song -> _currentSongFlow.emit(song) }
        }

        override suspend fun onSongPlaying() {
            play()
        }

        override suspend fun onSongPaused() {
            pause()
        }

        override suspend fun onSongCompilationEnd() {
            onSongPaused()
            // TODO: gonna change, when recommendations are implemented, so music continues playing after album ends
        }
    }
}