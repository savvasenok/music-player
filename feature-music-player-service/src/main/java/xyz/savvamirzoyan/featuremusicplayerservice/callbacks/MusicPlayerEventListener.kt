package xyz.savvamirzoyan.featuremusicplayerservice.callbacks

import android.widget.Toast
import com.google.android.exoplayer2.PlaybackException
import com.google.android.exoplayer2.Player
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import xyz.savvamirzoyan.featuremusicplayerservice.MusicPlayerService
import xyz.savvamirzoyan.musicplayer.usecaseplayermanager.RepeatModeDomain
import xyz.savvamirzoyan.musicplayer.usecaseplayermanager.UseCaseMusicPlayerManager

class MusicPlayerEventListener(
    private val musicPlayerService: MusicPlayerService,
    private val musicPlayerManager: UseCaseMusicPlayerManager,
    private val scope: CoroutineScope
) : Player.Listener {

    @Deprecated("Deprecated in Java")
    @Suppress("DEPRECATION")
    override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
        super.onPlayerStateChanged(playWhenReady, playbackState)
        if (playbackState == Player.STATE_READY && !playWhenReady) {
            musicPlayerService.stopForeground(false)
        }
    }

    override fun onPlayerError(error: PlaybackException) {
        super.onPlayerError(error)
        Toast.makeText(musicPlayerService, "Error happened", Toast.LENGTH_LONG).show()
    }

    override fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) {
        super.onShuffleModeEnabledChanged(shuffleModeEnabled)
        scope.launch { musicPlayerManager.updateShuffleMode(shuffleModeEnabled) }
    }

    override fun onRepeatModeChanged(repeatMode: Int) {
        super.onRepeatModeChanged(repeatMode)

        scope.launch {
            when (repeatMode) {
                Player.REPEAT_MODE_ALL -> musicPlayerManager.updateRepeatMode(RepeatModeDomain.REPEAT)
                Player.REPEAT_MODE_ONE -> musicPlayerManager.updateRepeatMode(RepeatModeDomain.REPEAT_ONCE)
                Player.REPEAT_MODE_OFF -> musicPlayerManager.updateRepeatMode(RepeatModeDomain.NONE)
            }
        }
    }
}