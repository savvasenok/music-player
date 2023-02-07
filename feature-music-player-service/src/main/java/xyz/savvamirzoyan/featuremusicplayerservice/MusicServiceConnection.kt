package xyz.savvamirzoyan.featuremusicplayerservice

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.PlaybackStateCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import xyz.savvamirzoyan.musicplayer.usecaseplayermanager.UseCaseMusicPlayerManager
import javax.inject.Inject

// wrapper class that tells about player states
class MusicServiceConnection @Inject constructor(
    @ApplicationContext context: Context,
    private val musicPlayerManager: UseCaseMusicPlayerManager,
    private val scope: CoroutineScope
) {

    lateinit var mediaController: MediaControllerCompat

    val transportControls: MediaControllerCompat.TransportControls
        get() = mediaController.transportControls

    private val mediaBrowserConnectionCallback = MediaBrowserConnectionCallback(context)

    private val mediaBrowser = MediaBrowserCompat(
        context,
        ComponentName(context, MusicPlayerService::class.java),
        mediaBrowserConnectionCallback,
        null
    ).apply { connect() }

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
        @SuppressLint("RestrictedApi")
        override fun onPlaybackStateChanged(state: PlaybackStateCompat?) {
            super.onPlaybackStateChanged(state)

            scope.launch {
                when (state?.state) {
                    PlaybackStateCompat.STATE_PLAYING -> musicPlayerManager.onSongPlaying()
                    PlaybackStateCompat.STATE_PAUSED -> musicPlayerManager.onSongPaused()
                    PlaybackStateCompat.STATE_STOPPED -> musicPlayerManager.onSongCompilationEnd()
                    PlaybackStateCompat.STATE_NONE -> {}
                    PlaybackStateCompat.STATE_ERROR -> {}
                    PlaybackStateCompat.STATE_BUFFERING -> {}
                    PlaybackStateCompat.STATE_REWINDING -> {}
                    PlaybackStateCompat.STATE_CONNECTING -> {}
                    PlaybackStateCompat.STATE_FAST_FORWARDING -> {}
                    PlaybackStateCompat.STATE_SKIPPING_TO_NEXT -> {}
                    PlaybackStateCompat.STATE_SKIPPING_TO_PREVIOUS -> {}
                    PlaybackStateCompat.STATE_SKIPPING_TO_QUEUE_ITEM -> {}
                }
            }
        }

        override fun onMetadataChanged(metadata: MediaMetadataCompat?) {
            super.onMetadataChanged(metadata)
            scope.launch {
                metadata?.description?.mediaId?.let { musicPlayerManager.updatePlayingCurrentSong(it) }
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