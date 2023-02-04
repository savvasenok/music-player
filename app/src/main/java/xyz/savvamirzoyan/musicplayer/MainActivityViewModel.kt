package xyz.savvamirzoyan.musicplayer

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import xyz.savvamirzoyan.musicplayer.appcore.CoreViewModel
import xyz.savvamirzoyan.musicplayer.usecaseplayermanager.UseCaseMusicPlayerManager
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    musicPLayerManager: UseCaseMusicPlayerManager
) : CoreViewModel() {

    val isSomethingPlayingFlow: Flow<Boolean> = musicPLayerManager.currentSongFlow
        .map { it != null }
        .distinctUntilChanged { old, new -> old == new }
}