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
    val currentSongProgressFlow: Flow<Int>
    val currentSongDurationFlow: Flow<Int>
    val isInShuffleModeFlow: Flow<Boolean>
    val repeatModeFlow: Flow<RepeatModeDomain>

    val skipBackFlow: Flow<Unit>
    val skipForwardFlow: Flow<Unit>
    val seekToSecondFlow: Flow<Int>

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
    suspend fun onSongProgress(currentProgress: Int, currentDuration: Int)
    suspend fun updateShuffleMode(isInShuffle: Boolean)
    suspend fun updateRepeatMode(mode: RepeatModeDomain)

    class Base @Inject constructor(private val musicRepository: MusicRepository) : UseCaseMusicPlayerManager {

        override var currentSongsCompilation: List<SongDomain> = mutableListOf()
            private set

        private val _isPlayingFlow = MutableStateFlow(false)
        override val isPlayingFlow: Flow<Boolean> = _isPlayingFlow

        private val _currentSongProgressFlow = MutableStateFlow(0)
        override val currentSongProgressFlow: Flow<Int> = _currentSongProgressFlow

        private val _currentSongDurationFlow = MutableStateFlow(0)
        override val currentSongDurationFlow: Flow<Int> = _currentSongDurationFlow

        private val _currentSongFlow = MutableStateFlow<SongDomain?>(null)
        override val currentSongFlow: Flow<SongDomain?> = _currentSongFlow

        private val _isInShuffleModeFlow = MutableStateFlow(false)
        override val isInShuffleModeFlow: Flow<Boolean> = _isInShuffleModeFlow

        private val _repeatModeFlow = MutableStateFlow(RepeatModeDomain.NONE)
        override val repeatModeFlow: Flow<RepeatModeDomain> = _repeatModeFlow

        private val _currentUserSelectedSongFlow = MutableSharedFlow<SongDomain>(replay = 0)
        override val currentUserSelectedSongFlow: Flow<SongDomain?> = _currentUserSelectedSongFlow

        private val _currentCompilationFlow = MutableStateFlow<SongCompilationDomain?>(null)
        override val currentCompilationFlow: Flow<SongCompilationDomain?> = _currentCompilationFlow

        private val _skipBackFlow = MutableSharedFlow<Unit>(replay = 0)
        override val skipBackFlow: Flow<Unit> = _skipBackFlow

        private val _skipForwardFlow = MutableSharedFlow<Unit>(replay = 0)
        override val skipForwardFlow: Flow<Unit> = _skipForwardFlow

        private val _seekToSecondFlow = MutableSharedFlow<Int>(replay = 0)
        override val seekToSecondFlow: Flow<Int> = _seekToSecondFlow

        override suspend fun getSong(songId: StringID): SongDomain =
            musicRepository.getSong(songId)
                ?: throw IllegalArgumentException("No song with this id ($songId)")

        override suspend fun getCompilation(compilationId: StringID): SongCompilationDomain =
            musicRepository.getSongCompilation(compilationId)
                ?: throw IllegalArgumentException("No song compilation with this id ($compilationId)")

        override suspend fun pause() = _isPlayingFlow.emit(false)
        override suspend fun play() = _isPlayingFlow.emit(true)

        override suspend fun play(songId: StringID) {
            musicRepository.getSong(songId)?.also { song -> play(song) }
        }

        private suspend fun play(song: SongDomain) {
            val compilation = song.let { musicRepository.getSongCompilation(song.compilationId) }

            currentSongsCompilation = compilation?.songs ?: emptyList()

            _currentSongFlow.emit(song)
            _currentUserSelectedSongFlow.emit(song)
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
                    ?.also { compilation -> play(compilation.songs.first()) }
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

        override suspend fun onSongProgress(currentProgress: Int, currentDuration: Int) {
            _currentSongDurationFlow.emit(currentDuration / 1000)
            _currentSongProgressFlow.emit(((currentProgress.toFloat() / currentDuration) * 100).toInt())
        }

        override suspend fun updateShuffleMode(isInShuffle: Boolean) {
            _isInShuffleModeFlow.emit(isInShuffle)
        }

        override suspend fun updateRepeatMode(mode: RepeatModeDomain) {
            _repeatModeFlow.emit(mode)
        }
    }
}