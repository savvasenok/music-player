package xyz.savvamirzoyan.musicplayer.appcore

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull

abstract class CoreViewModel : ViewModel() {

    private val _loadingFlow = MutableStateFlow<Boolean?>(null)
    val loadingFlow: Flow<Boolean> = _loadingFlow.filterNotNull()

    protected suspend fun whileLoading(function: suspend () -> Unit) {
        _loadingFlow.emit(true)
        function()
        _loadingFlow.emit(false)
    }
}