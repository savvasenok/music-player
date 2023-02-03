package xyz.savvamirzoyan.featuremusicplayerservice.ui

import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaBrowserCompat.SubscriptionCallback
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import xyz.savvamirzoyan.featuremusicplayerservice.MusicPlayerService.Companion.MEDIA_ROOT_ID
import xyz.savvamirzoyan.featuremusicplayerservice.MusicServiceConnection
import xyz.savvamirzoyan.musicplayer.appcore.SongService
import xyz.savvamirzoyan.musicplayer.core.StringID
import xyz.savvamirzoyan.musicplayer.usecaseplayermanager.UseCaseMusicPlayerManager
import javax.inject.Inject

// class to manage player from local UI (buttons, seekbars etc)
class MusicPlayerManager @Inject constructor(
    private val musicPlayerManagerUseCase: UseCaseMusicPlayerManager,
    private val musicServiceConnection: MusicServiceConnection,
    scope: CoroutineScope
) {

    init {
        scope.launch {
            musicServiceConnection.subscribe(MEDIA_ROOT_ID, object : SubscriptionCallback() {
                override fun onChildrenLoaded(parentId: String, children: MutableList<MediaBrowserCompat.MediaItem>) {
                    super.onChildrenLoaded(parentId, children)
                    children.map {
                        SongService(
                            it.mediaId!!,
                            it.description.title.toString(),
                            it.description.subtitle.toString(),
                            it.description.mediaUri.toString(),
                            it.description.iconUri.toString()
                        )
                    }
                }
            })
        }

        scope.launch {
            musicPlayerManagerUseCase.currentUserSelectedSongFlow.collect {
                it?.also { playSong(it.id) }
            }
        }

        scope.launch {
            musicPlayerManagerUseCase.isPlayingFlow.collect { isPlaying ->
                if (isPlaying) continuePlaying()
                else pauseSong()
            }
        }
    }

    @Suppress("unused")
    fun skipToNextSong() {
        musicServiceConnection.transportControls.skipToNext()
    }

    @Suppress("unused")
    fun skipToPreviousSong() {
        musicServiceConnection.transportControls.skipToPrevious()
    }

    @Suppress("unused")
    fun seekTo(pos: Long) {
        musicServiceConnection.transportControls.seekTo(pos)
    }

    private fun pauseSong() {
        try {
            musicServiceConnection.transportControls.pause()
        } catch (e: UninitializedPropertyAccessException) {
            // reached when app opens 1st time and it tries to stop music
        }
    }

    private fun continuePlaying() {
        musicServiceConnection.transportControls.play()
    }

    private fun playSong(songID: StringID) {
        musicServiceConnection.transportControls.playFromMediaId(songID, null)
    }

    fun onClear() {
        musicServiceConnection.unsubscribe(MEDIA_ROOT_ID, object : SubscriptionCallback() {})
    }
}