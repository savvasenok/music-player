package xyz.savvamirzoyan.featuremusicplayerservice.callbacks

import android.widget.Toast
import com.google.android.exoplayer2.PlaybackException
import com.google.android.exoplayer2.Player
import xyz.savvamirzoyan.featuremusicplayerservice.MusicPlayerService

class MusicPlayerEventListener(
    private val musicPlayerService: MusicPlayerService,

    ) : Player.Listener {

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
}