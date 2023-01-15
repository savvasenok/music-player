package xyz.savvamirzoyan.musicplayer.appcore

import android.net.Uri
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.*

abstract class CoreViewModel : ViewModel() {

    private val _loadingFlow = MutableStateFlow<Boolean?>(null)
    val loadingFlow: Flow<Boolean> = _loadingFlow.filterNotNull()

    private val _navigationDeeplinkFlow = MutableSharedFlow<String>(replay = 0)
    val navigationDeeplinkFlow: Flow<Uri> = _navigationDeeplinkFlow
        .map { Uri.parse(it) }

    protected suspend fun whileLoading(function: suspend () -> Unit) {
        _loadingFlow.emit(true)
        function()
        _loadingFlow.emit(false)
    }

    protected suspend fun navigate(deeplink: String) = _navigationDeeplinkFlow.emit(deeplink)
}