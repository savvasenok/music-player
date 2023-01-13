package xyz.savvamirzoyan.featuremusicplayerservice.callbacks

import android.net.Uri
import android.os.Bundle
import android.os.ResultReceiver
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import xyz.savvamirzoyan.musicplayer.usecaseplayermanager.UseCaseMusicPlayerManager

class MusicPlaybackPreparer(
//    private val deezerMusicSource: DeezerMusicSource,
    private val musicPlayerManager: UseCaseMusicPlayerManager,
    private val scope: CoroutineScope,
    private val playerPrepared: (MediaMetadataCompat?) -> Unit
) : MediaSessionConnector.PlaybackPreparer {

    override fun onCommand(player: Player, command: String, extras: Bundle?, cb: ResultReceiver?) = false

    override fun getSupportedPrepareActions(): Long {
        return PlaybackStateCompat.ACTION_PREPARE_FROM_MEDIA_ID or
                PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID
    }

    override fun onPrepare(playWhenReady: Boolean) = Unit

    override fun onPrepareFromMediaId(mediaId: String, playWhenReady: Boolean, extras: Bundle?) {

        Log.d("SPAMEGGS", "onPrepareFromMediaId(mediaId:$mediaId, playWhenReady:$playWhenReady)")

        scope.launch {
            Log.d("SPAMEGGS", "launching coroutine in onPrepareFromMediaId")
            musicPlayerManager.getSong(mediaId)
                .let {
                    Log.d("SPAMEGGS", "songDomain:$it")
                    MediaMetadataCompat.Builder()
                        .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, it.artist)
                        .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, it.id.toString())
                        .putString(MediaMetadataCompat.METADATA_KEY_TITLE, it.title)
                        .putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_TITLE, it.title)
                        .putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_ICON_URI, it.albumPictureUrl)
                        .putString(MediaMetadataCompat.METADATA_KEY_ALBUM_ART_URI, it.albumPictureUrl)
                        .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_URI, it.songUrl)
                        .putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_SUBTITLE, it.artist)
                        .putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_DESCRIPTION, it.artist)
                        .build()
                }
                .also { playerPrepared(it) }
        }

//        val itemToPlay = deezerMusicSource.songs.find {
//            mediaId == it.description.mediaId
//        }
//        playerPrepared(itemToPlay)
    }

    override fun onPrepareFromSearch(query: String, playWhenReady: Boolean, extras: Bundle?) = Unit

    override fun onPrepareFromUri(uri: Uri, playWhenReady: Boolean, extras: Bundle?) = Unit
}