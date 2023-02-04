package xyz.savvamirzoyan.musicplayer.crash

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import xyz.savvamirzoyan.musicplayer.R
import xyz.savvamirzoyan.musicplayer.appcore.CoreViewModel
import xyz.savvamirzoyan.musicplayer.appcore.uistate.ButtonState
import xyz.savvamirzoyan.musicplayer.appcore.uistate.TextValue
import javax.inject.Inject

@HiltViewModel
class CrashViewModel @Inject constructor(
    // inject firebase crashlytics to report error
) : CoreViewModel() {

    private val _actionFinishActivityFlow = MutableSharedFlow<Unit>(replay = 0)
    val actionFinishActivityFlow: Flow<Unit> = _actionFinishActivityFlow

    private val _buttonStateFlow = MutableStateFlow(ButtonState(text = TextValue(R.string.report)))
    val buttonStateFlow: Flow<ButtonState> = _buttonStateFlow

    fun reportError(throwable: Throwable?) {
        viewModelScope.launch {

            _buttonStateFlow.emit(ButtonState(text = TextValue(R.string.report_progress), isEnabled = false))
            delay(3000)
            _buttonStateFlow.emit(ButtonState(text = TextValue(R.string.report_finish), isEnabled = false))
            delay(1500)
            _actionFinishActivityFlow.emit(Unit)
        }
    }
}