@file:Suppress("DEPRECATION")

package xyz.savvamirzoyan.featuremusicplayerservice

import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import androidx.media.MediaBrowserServiceCompat
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector
import com.google.android.exoplayer2.ext.mediasession.TimelineQueueNavigator
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import xyz.savvamirzoyan.featuremusicplayerservice.callbacks.MusicPlaybackPreparer
import xyz.savvamirzoyan.featuremusicplayerservice.callbacks.MusicPlayerEventListener
import xyz.savvamirzoyan.featuremusicplayerservice.callbacks.MusicPlayerNotificationListener
import xyz.savvamirzoyan.musicplayer.usecaseplayermanager.UseCaseMusicPlayerManager
import javax.inject.Inject

@AndroidEntryPoint
class MusicPlayerService : MediaBrowserServiceCompat() {

    @Suppress("DEPRECATION")
    @Inject
    lateinit var dataSourceFactory: DefaultDataSourceFactory

    @Inject
    lateinit var exoPlayer: ExoPlayer

    @Inject
    lateinit var serviceScope: CoroutineScope

    @Inject
    lateinit var musicSource: MusicSource

    @Inject
    lateinit var musicPlayerManager: UseCaseMusicPlayerManager

    private lateinit var musicNotificationManager: MusicNotificationManager
    private lateinit var mediaSession: MediaSessionCompat
    private lateinit var mediaSessionConnector: MediaSessionConnector
    private lateinit var musicPlayerEventListener: MusicPlayerEventListener
    private lateinit var songDomainToMediaMetadataMapper: SongDomainToMediaMetadataMapper

    var isForegroundService = false

    override fun onCreate() {
        super.onCreate()

        songDomainToMediaMetadataMapper = SongDomainToMediaMetadataMapper()

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
        )

        val musicPlaybackPreparer = MusicPlaybackPreparer(
            musicPlayerManager,
            serviceScope,
            songDomainToMediaMetadataMapper
        ) { songs, playWhenReady, songIndex -> preparePlayer(songs, playWhenReady, songIndex) }

        mediaSessionConnector = MediaSessionConnector(mediaSession)
        mediaSessionConnector.setPlaybackPreparer(musicPlaybackPreparer)
        mediaSessionConnector.setQueueNavigator(MusicQueueNavigator())
        mediaSessionConnector.setPlayer(exoPlayer)

        musicPlayerEventListener = MusicPlayerEventListener(this, musicPlayerManager, serviceScope)
        exoPlayer.addListener(musicPlayerEventListener)
        musicNotificationManager.showNotification(exoPlayer)

        serviceScope.launch {
            while (true) {

                exoPlayer.mediaItemCount

                delay(250)
                musicPlayerManager.onSongProgress(
                    exoPlayer.currentPosition.toInt(),
                    exoPlayer.duration.toInt()
                )
            }
        }
    }

    private fun preparePlayer(
        songs: List<MediaMetadataCompat>,
        playWhenReady: Boolean,
        songIndex: Int,
    ) {
        exoPlayer.setMediaSource(musicSource.asMediaSource(songs, dataSourceFactory))
        exoPlayer.prepare()
        exoPlayer.seekTo(songIndex, 0L)
        exoPlayer.playWhenReady = playWhenReady
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
            MEDIA_ROOT_ID -> result.sendResult(mutableListOf())
        }
    }

    private inner class MusicQueueNavigator : TimelineQueueNavigator(mediaSession) {
        override fun getMediaDescription(player: Player, windowIndex: Int): MediaDescriptionCompat {
            return musicPlayerManager.currentSongsCompilation[windowIndex]
                .let { songDomainToMediaMetadataMapper.map(it) }
                .let { musicSource.asMediaItems(listOf(it)) }
                .first()
                .description
        }
    }

    companion object {
        private const val TAG_SERVICE = "SAVVASENOK_SPOTIFY_PLAYER"
        const val MEDIA_ROOT_ID = "root_id"
    }
}