package xyz.savvamirzoyan.musicplayer.featurehome

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import xyz.savvamirzoyan.musicplayer.appcore.CoreViewModel
import xyz.savvamirzoyan.musicplayer.appcore.DeepLinkBuilder
import xyz.savvamirzoyan.musicplayer.appcore.uistate.TextValue
import xyz.savvamirzoyan.musicplayer.core.StringID
import xyz.savvamirzoyan.musicplayer.featurehome.model.LastPlaylistsStateUi
import xyz.savvamirzoyan.musicplayer.featurehome.model.ToolbarChipsStateUi
import xyz.savvamirzoyan.musicplayer.usecasedatetime.DateTimeUseCase
import xyz.savvamirzoyan.musicplayer.usecaseplayermanager.UseCaseMusicPlayerManager
import xyz.savvamirzoyan.musicplayer.usecaseplayhistory.PlayHistoryUseCase
import xyz.savvamirzoyan.musicplayer.usecaseplayhistory.model.LastPlayedPlaylistDomain
import javax.inject.Inject

private const val LAST_PLAYED_SONGS_COUNT = 5

@HiltViewModel
internal class HomeViewModel @Inject constructor(
    private val datetimeUseCase: DateTimeUseCase,
    private val playHistoryUseCase: PlayHistoryUseCase,
    private val currentPartOfDayToTextValueMapper: CurrentPartOfDayToTextValueMapper,
    private val lastPlayedPlaylistToLastPlaylistsStateMapper: LastPlayedPlaylistToLastPlaylistsStateMapper,
    private val musicPlayerManager: UseCaseMusicPlayerManager,
    private val deepLinkBuilder: DeepLinkBuilder
) : CoreViewModel() {

    private val initToolbarChipsState = ToolbarChipsStateUi(
        isCancelChipVisible = false,
        isMusicChipVisible = true,
        isMusicSelected = false,
        isPodcastsAndShowsChipVisible = true,
        isPodcastsAndShowsChipSelected = false
    )

    private val _toolbarGreetingTextFlow = MutableStateFlow<TextValue?>(null)
    val toolbarGreetingTextFlow: Flow<TextValue> = _toolbarGreetingTextFlow.filterNotNull()

    private val _toolbarChipsStateFlow = MutableStateFlow(initToolbarChipsState)
    internal val toolbarChipsStateFlow: Flow<ToolbarChipsStateUi> = _toolbarChipsStateFlow

    private val _lastPlayedCompilationsFlow = MutableStateFlow<List<LastPlayedPlaylistDomain>>(emptyList())
    internal val lastPlayedCompilationsFlow: Flow<LastPlaylistsStateUi> =
        combine(
            _lastPlayedCompilationsFlow,
            musicPlayerManager.currentCompilationFlow,
            musicPlayerManager.isPlayingFlow
        ) { lastPlayedCompilation, currentCompilation, isPlaying ->
            lastPlayedPlaylistToLastPlaylistsStateMapper.map(
                lastPlayedCompilation,
                if (isPlaying) currentCompilation?.id else null
            )
        }

    private val _lastPlayedSongsIdsFlow = MutableSharedFlow<List<StringID>>(replay = 0)
    val lastPlayedSongsStateFlow: Flow<List<StringID>> = _lastPlayedSongsIdsFlow
        .filter { it.isNotEmpty() }
        .map { it.take(LAST_PLAYED_SONGS_COUNT) }
    val isLastSongsSectionVisible: Flow<Boolean> = _lastPlayedSongsIdsFlow
        .map { it.isNotEmpty() }

    init {
        setupGreetings()
        setupLastPlayedCompilations()
        setupLastPlayedSongs()
    }

    private fun setupGreetings() {
        viewModelScope.launch {
            datetimeUseCase.getCurrentPartOfTheDay()
                .let { currentPartOfDayToTextValueMapper.map(it) }
                .let { _toolbarGreetingTextFlow.emit(it) }
        }
    }

    private fun setupLastPlayedCompilations() {
        viewModelScope.launch {
            whileLoading {
                playHistoryUseCase.getLastPlayedPlaylists().let { _lastPlayedCompilationsFlow.emit(it) }
            }
        }
    }

    private fun setupLastPlayedSongs() {
        viewModelScope.launch {
            playHistoryUseCase.getLastPlayedSongs()
                .map { it.id }
                .also { _lastPlayedSongsIdsFlow.emit(it) }
        }
    }

    fun onCompilationClick(compilationIndex: Int) {
        viewModelScope.launch {
            playHistoryUseCase.getLastPlayedPlaylists()[compilationIndex]
                .let { playlist -> deepLinkBuilder.buildPlaylistDeepLink(playlist.id) }
                .also { navigate(it) }
        }
    }
}