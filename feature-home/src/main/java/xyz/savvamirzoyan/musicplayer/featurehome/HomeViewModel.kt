package xyz.savvamirzoyan.musicplayer.featurehome

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import xyz.savvamirzoyan.musicplayer.appcore.CoreViewModel
import xyz.savvamirzoyan.musicplayer.appcore.TextValue
import xyz.savvamirzoyan.musicplayer.core.StringID
import xyz.savvamirzoyan.musicplayer.featurehome.model.LastPlaylistsStateUi
import xyz.savvamirzoyan.musicplayer.featurehome.model.ToolbarChipsStateUi
import xyz.savvamirzoyan.musicplayer.usecasedatetime.DateTimeUseCase
import xyz.savvamirzoyan.musicplayer.usecaseplayhistory.PlayHistoryUseCase
import javax.inject.Inject

@HiltViewModel
internal class HomeViewModel @Inject constructor(
    private val datetimeUseCase: DateTimeUseCase,
    private val playHistoryUseCase: PlayHistoryUseCase,
    private val currentPartOfDayToTextValueMapper: CurrentPartOfDayToTextValueMapper,
    private val lastPlayedPlaylistToLastPlaylistsStateMapper: LastPlayedPlaylistToLastPlaylistsStateMapper,
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

    private val _playlistsStateFlow = MutableStateFlow(LastPlaylistsStateUi())
    internal val playlistsStateFlow: Flow<LastPlaylistsStateUi> = _playlistsStateFlow

    private val _lastPlayedSongsIdsFlow = MutableStateFlow<List<StringID>>(emptyList())
    val lastPlayedSongsStateFlow: Flow<List<StringID>> = _lastPlayedSongsIdsFlow
    val isLastSongsSectionVisible: Flow<Boolean> = _lastPlayedSongsIdsFlow.map { it.isNotEmpty() }

    init {
        setupGreetings()
        setupLastPlayedPlaylists()
        setupLastPlayedSongs()
    }

    private fun setupGreetings() {
        viewModelScope.launch {
            datetimeUseCase.getCurrentPartOfTheDay()
                .let { currentPartOfDayToTextValueMapper.map(it) }
                .let { _toolbarGreetingTextFlow.emit(it) }
        }
    }

    private fun setupLastPlayedPlaylists() {
        viewModelScope.launch {
            whileLoading {
                playHistoryUseCase.getLastPlayedPlaylists()
                    .let { lastPlayedPlaylistToLastPlaylistsStateMapper.map(it) }
                    .let { _playlistsStateFlow.emit(it) }
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
}