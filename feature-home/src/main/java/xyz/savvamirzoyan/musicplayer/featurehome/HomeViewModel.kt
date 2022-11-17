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
import xyz.savvamirzoyan.musicplayer.featurehome.model.LastPlayedSongsStateUi
import xyz.savvamirzoyan.musicplayer.featurehome.model.LastPlaylistsStateUi
import xyz.savvamirzoyan.musicplayer.featurehome.model.ToolbarChipsStateUi
import xyz.savvamirzoyan.musicplayer.usecasedatetime.DateTimeUseCase
import xyz.savvamirzoyan.musicplayer.usecaseplayhistory.PlayHistoryUseCase
import javax.inject.Inject

@HiltViewModel
internal class HomeViewModel @Inject constructor(
    private val datetimeUseCase: DateTimeUseCase,
    private val currentPartOfDayToTextValueMapper: CurrentPartOfDayToTextValueMapper,
    private val playHistoryUseCase: PlayHistoryUseCase,
    private val lastPlayedPlaylistToLastPlaylistsStateMapper: LastPlayedPlaylistToLastPlaylistsStateMapper,
    private val lastPlayedSongDomainToUiMapper: LastPlayedSongDomainToUiMapper
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

    private val _lastPlayedSongsStateFlow = MutableStateFlow(LastPlayedSongsStateUi(emptyList()))
    val lastPlayedSongsStateFlow: Flow<LastPlayedSongsStateUi> = _lastPlayedSongsStateFlow
    val isLastSongsSectionVisible: Flow<Boolean> = _lastPlayedSongsStateFlow.map { it.songs.isNotEmpty() }

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
            playHistoryUseCase.getLastPlayedPlaylists()
                .let { lastPlayedPlaylistToLastPlaylistsStateMapper.map(it) }
                .let { _playlistsStateFlow.emit(it) }
        }
    }

    private fun setupLastPlayedSongs() {
        viewModelScope.launch {
            playHistoryUseCase.getLastPlayedSongs()
                .map { lastPlayedSongDomainToUiMapper.map(it) }
                .let { LastPlayedSongsStateUi(it) }
                .let { _lastPlayedSongsStateFlow.emit(it) }
        }
    }
}