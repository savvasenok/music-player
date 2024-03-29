package xyz.savvamirzoyan.featuremusicplayerservice.callbacks

import android.net.Uri
import android.os.Bundle
import android.os.ResultReceiver
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.PlaybackStateCompat
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import xyz.savvamirzoyan.featuremusicplayerservice.SongDomainToMediaMetadataMapper
import xyz.savvamirzoyan.musicplayer.usecaseplayermanager.UseCaseMusicPlayerManager

class MusicPlaybackPreparer(
    private val musicPlayerManager: UseCaseMusicPlayerManager,
    private val scope: CoroutineScope,
    private val songDomainToMediaMetadataMapper: SongDomainToMediaMetadataMapper,
    private val playerPrepared: (songs: List<MediaMetadataCompat>, playWhenReady: Boolean, songIndex: Int) -> Unit
) : MediaSessionConnector.PlaybackPreparer {

    override fun onCommand(player: Player, command: String, extras: Bundle?, cb: ResultReceiver?) = false

    override fun getSupportedPrepareActions(): Long {
        return PlaybackStateCompat.ACTION_PREPARE_FROM_MEDIA_ID or
                PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID
    }

    override fun onPrepare(playWhenReady: Boolean) = Unit

    override fun onPrepareFromMediaId(mediaId: String, playWhenReady: Boolean, extras: Bundle?) {
        scope.launch {
            val songIndex: Int
            (musicPlayerManager.currentCompilationFlow.firstOrNull()?.songs ?: emptyList())
                .also { songs -> songIndex = songs.indexOfFirst { song -> song.id == mediaId } }
                .map { song -> songDomainToMediaMetadataMapper.map(song) }
                .also { playerPrepared(it, playWhenReady, songIndex) }
        }
    }

    override fun onPrepareFromSearch(query: String, playWhenReady: Boolean, extras: Bundle?) = Unit

    override fun onPrepareFromUri(uri: Uri, playWhenReady: Boolean, extras: Bundle?) = Unit
}