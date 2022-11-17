package xyz.savvamirzoyan.musicplayer.featurehome

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import xyz.savvamirzoyan.musicplayer.appcore.CoreViewModel
import xyz.savvamirzoyan.musicplayer.appcore.TextValue
import xyz.savvamirzoyan.musicplayer.featurehome.model.ToolbarChipsStateUi
import xyz.savvamirzoyan.musicplayer.usecasedatetime.DateTimeUseCase
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val datetimeUseCase: DateTimeUseCase,
    private val currentPartOfDayToTextValueMapper: CurrentPartOfDayToTextValueMapper,
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

    private val _playlistsStateFlow = MutableStateFlow(LastPlaylistsStateUi())
    internal val playlistsStateFlow: Flow<LastPlaylistsStateUi> = _playlistsStateFlow

    private val _toolbarChipsStateFlow = MutableStateFlow(initToolbarChipsState)
    internal val toolbarChipsStateFlow: Flow<ToolbarChipsStateUi> = _toolbarChipsStateFlow

    init {
        setupGreetings()
    }

    private fun setupGreetings() {
        viewModelScope.launch {
            val responseDomain = datetimeUseCase.getCurrentPartOfTheDay()
            val textValue = currentPartOfDayToTextValueMapper.map(responseDomain)
            _toolbarGreetingTextFlow.emit(textValue)
        }
    }
}