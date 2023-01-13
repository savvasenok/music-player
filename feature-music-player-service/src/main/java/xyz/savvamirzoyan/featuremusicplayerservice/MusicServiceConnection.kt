package xyz.savvamirzoyan.featuremusicplayerservice

import android.content.ComponentName
import android.content.Context
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.PlaybackStateCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import javax.inject.Inject

class MusicServiceConnection @Inject constructor(
    @ApplicationContext context: Context,
    private val scope: CoroutineScope
) {
    private val _isConnectedFlow = MutableSharedFlow<Boolean>(replay = 0)
    val isConnectedFlow: Flow<Boolean> = _isConnectedFlow

    private val _networkErrorFlow = MutableSharedFlow<Boolean>(replay = 0)
    val networkErrorFlow: Flow<Boolean> = _networkErrorFlow

    private val _playbackStateFlow = MutableStateFlow<PlaybackStateCompat?>(null)
    val playbackStateFlow: Flow<PlaybackStateCompat?> = _playbackStateFlow

    private val _currentlyPlayingSongFlow = MutableStateFlow<MediaMetadataCompat?>(null)
    val currentlyPlayingSongFlow: Flow<MediaMetadataCompat> = _currentlyPlayingSongFlow.filterNotNull()

    lateinit var mediaController: MediaControllerCompat

    private val mediaBrowserConnectionCallback = MediaBrowserConnectionCallback(context)

    val transportControls: MediaControllerCompat.TransportControls
        get() = mediaController.transportControls

    private val mediaBrowser = MediaBrowserCompat(
        context,
        ComponentName(context, MusicPlayerService::class.java),
        mediaBrowserConnectionCallback,
        null
    ).apply { connect() }

    fun subscribe(parentId: String, callback: MediaBrowserCompat.SubscriptionCallback) {
        mediaBrowser.subscribe(parentId, callback)
    }

    fun unsubscribe(parentId: String, callback: MediaBrowserCompat.SubscriptionCallback) {
        mediaBrowser.unsubscribe(parentId, callback)
    }

    private inner class MediaBrowserConnectionCallback(
        private val context: Context
    ) : MediaBrowserCompat.ConnectionCallback() {

        override fun onConnected() {
            mediaController = MediaControllerCompat(context, mediaBrowser.sessionToken).apply {
                registerCallback(MediaControllerCallback())
            }

            _isConnectedFlow.tryEmit(true)
        }

        override fun onConnectionSuspended() {
            _isConnectedFlow.tryEmit(false)
        }

        override fun onConnectionFailed() {
            _isConnectedFlow.tryEmit(false)
        }
    }

    private inner class MediaControllerCallback : MediaControllerCompat.Callback() {
        override fun onPlaybackStateChanged(state: PlaybackStateCompat?) {
            super.onPlaybackStateChanged(state)
            scope.launch {
                _playbackStateFlow.emit(state)
            }
        }

        override fun onMetadataChanged(metadata: MediaMetadataCompat?) {
            super.onMetadataChanged(metadata)

            scope.launch {
                _currentlyPlayingSongFlow.emit(metadata)
            }
        }

        override fun onSessionEvent(event: String?, extras: Bundle?) {
            super.onSessionEvent(event, extras)

            when (event) {
                NETWORK_ERROR -> _networkErrorFlow.tryEmit(false)
            }
        }

        override fun onSessionDestroyed() {
            mediaBrowserConnectionCallback.onConnectionSuspended()
        }
    }

    companion object {
        const val NETWORK_ERROR = "NETWORK_ERROR"
    }
}