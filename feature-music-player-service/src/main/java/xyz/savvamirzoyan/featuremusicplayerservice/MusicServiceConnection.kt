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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import xyz.savvamirzoyan.musicplayer.usecaseplayermanager.UseCaseMusicPlayerManager
import javax.inject.Inject

// wrapper class that tells about player states
class MusicServiceConnection @Inject constructor(
    @ApplicationContext context: Context,
    private val musicPlayerManager: UseCaseMusicPlayerManager,
    private val scope: CoroutineScope
) {

    private val _playbackStateFlow = MutableStateFlow<PlaybackStateCompat?>(null)
//    val playbackStateFlow: Flow<PlaybackStateCompat?> = _playbackStateFlow
//    val isPlayingNowFlow: Flow<Boolean> = playbackStateFlow
//        .map { it?.isPlaying == true }
//        .distinctUntilChanged { old, new -> old == new }
//        .onEach { if (it) musicPlayerManager.play() else musicPlayerManager.pause() }

//    private val _currentlyPlayingSongFlow = MutableStateFlow<MediaMetadataCompat?>(null)
//    val currentlyPlayingSongIdFlow: Flow<String?> = _currentlyPlayingSongFlow
//        .distinctUntilChanged { old, new -> old?.description?.mediaId == new?.description?.mediaId }
//        .map { it?.description?.mediaId }
//        .onEach { mediaId -> mediaId?.let { musicPlayerManager.updatePlayingCurrentSong(mediaId) } }

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

        override fun onConnectionSuspended() {}
        override fun onConnectionFailed() {}
        override fun onConnected() {
            mediaController = MediaControllerCompat(context, mediaBrowser.sessionToken).apply {
                registerCallback(MediaControllerCallback())
            }
        }
    }

    private inner class MediaControllerCallback : MediaControllerCompat.Callback() {
        override fun onPlaybackStateChanged(state: PlaybackStateCompat?) {
            super.onPlaybackStateChanged(state)
            scope.launch { _playbackStateFlow.emit(state) }
        }

        override fun onMetadataChanged(metadata: MediaMetadataCompat?) {
            super.onMetadataChanged(metadata)
            scope.launch {
                metadata?.description?.mediaId?.let { musicPlayerManager.updatePlayingCurrentSong(it) }
//                _currentlyPlayingSongFlow.emit(metadata)
            }
        }

        override fun onSessionEvent(event: String?, extras: Bundle?) {
            super.onSessionEvent(event, extras)

            when (event) {
                NETWORK_ERROR -> {}
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