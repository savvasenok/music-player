package xyz.savvamirzoyan.featuremusicplayerservice

import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.util.Log
import androidx.media.MediaBrowserServiceCompat
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector
import com.google.android.exoplayer2.ext.mediasession.TimelineQueueNavigator
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import xyz.savvamirzoyan.featuremusicplayerservice.MusicServiceConnection.Companion.NETWORK_ERROR
import xyz.savvamirzoyan.featuremusicplayerservice.callbacks.MusicPlaybackPreparer
import xyz.savvamirzoyan.featuremusicplayerservice.callbacks.MusicPlayerEventListener
import xyz.savvamirzoyan.featuremusicplayerservice.callbacks.MusicPlayerNotificationListener
import xyz.savvamirzoyan.musicplayer.usecaseplayermanager.UseCaseMusicPlayerManager
import javax.inject.Inject

@AndroidEntryPoint
class MusicPlayerService : MediaBrowserServiceCompat() {

    @Inject
    lateinit var dataSourceFactory: DefaultDataSourceFactory

    @Inject
    lateinit var exoPlayer: ExoPlayer

    @Inject
    lateinit var deezerMusicSource: DeezerMusicSource

    @Inject
    lateinit var serviceScope: CoroutineScope

    @Inject
    lateinit var musicPlayerManager: UseCaseMusicPlayerManager

    private lateinit var musicNotificationManager: MusicNotificationManager
    private lateinit var mediaSession: MediaSessionCompat
    private lateinit var mediaSessionConnector: MediaSessionConnector
    private lateinit var musicPlayerEventListener: MusicPlayerEventListener

    var isForegroundService = false

    private var currentlyPlayingSong: MediaMetadataCompat? = null
    private var isPlayerInitialized = false

    override fun onCreate() {
        super.onCreate()

        serviceScope.launch {
            deezerMusicSource.fetchMediaData()
        }

        // intent to open activity, when clicked on notification
        val activityIntent = packageManager?.getLaunchIntentForPackage(packageName)
            ?.let { PendingIntent.getActivity(this, 0, it, PendingIntent.FLAG_IMMUTABLE) }

        mediaSession = MediaSessionCompat(this, TAG_SERVICE).apply {
            setSessionActivity(activityIntent)
            isActive = true
        }

        sessionToken = mediaSession.sessionToken

        musicNotificationManager = MusicNotificationManager(
            this,
            mediaSession.sessionToken,
            MusicPlayerNotificationListener(this)
        ) {
            currentSongDuration = exoPlayer.duration
        }

        val musicPlaybackPreparer = MusicPlaybackPreparer(musicPlayerManager, serviceScope/*deezerMusicSource*/) {
            currentlyPlayingSong = it
            preparePlayer(/*deezerMusicSource.songs, */it, true)
        }

        mediaSessionConnector = MediaSessionConnector(mediaSession)
        mediaSessionConnector.setPlaybackPreparer(musicPlaybackPreparer)
        mediaSessionConnector.setQueueNavigator(MusicQueueNavigator())
        mediaSessionConnector.setPlayer(exoPlayer)

        musicPlayerEventListener = MusicPlayerEventListener(this)
        exoPlayer.addListener(musicPlayerEventListener)
        musicNotificationManager.showNotification(exoPlayer)
    }

    private fun preparePlayer(
//        songs: List<MediaMetadataCompat>,
        itemToPlay: MediaMetadataCompat?,
        playNow: Boolean
    ) {
        Log.d("SPAMEGGS", "preparePlayer(itemToPlay:$itemToPlay)")

//        val currentSongIndex = if (currentlyPlayingSong == null) 0 else songs.indexOf(itemToPlay)
        if (itemToPlay != null) {
            exoPlayer.setMediaSource(deezerMusicSource.asMediaSource(itemToPlay, dataSourceFactory))
            exoPlayer.prepare()
            exoPlayer.seekTo(/*currentSongIndex*/0, 0L)
            exoPlayer.playWhenReady = playNow
        }
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        exoPlayer.stop()
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
        exoPlayer.release()
        exoPlayer.removeListener(musicPlayerEventListener)
    }

    override fun onGetRoot(clientPackageName: String, clientUid: Int, rootHints: Bundle?) =
        BrowserRoot(MEDIA_ROOT_ID, null)

    override fun onLoadChildren(parentId: String, result: Result<MutableList<MediaBrowserCompat.MediaItem>>) {
        when (parentId) {
            MEDIA_ROOT_ID -> {
                val resultsSent = deezerMusicSource.whenReady { isInitialized ->
                    if (isInitialized) {
                        result.sendResult(deezerMusicSource.asMediaItems())

                        if (!isPlayerInitialized && deezerMusicSource.songs.isNotEmpty()) {
                            preparePlayer(/*deezerMusicSource.songs,*/ null, false)
                            isPlayerInitialized = true
                        }
                    } else {
                        mediaSession.sendSessionEvent(NETWORK_ERROR, null)
                        result.sendResult(null)
                    }
                }

                if (!resultsSent) {
                    result.detach()
                }
            }
        }
    }

    private inner class MusicQueueNavigator : TimelineQueueNavigator(mediaSession) {
        override fun getMediaDescription(player: Player, windowIndex: Int): MediaDescriptionCompat {
            return deezerMusicSource.songs[windowIndex].description
        }
    }

    companion object {
        private const val TAG_SERVICE = "SAVVASENOK_SPOTIFY_PLAYER"
        const val MEDIA_ROOT_ID = "root_id"

        var currentSongDuration = 0L
            private set
    }
}