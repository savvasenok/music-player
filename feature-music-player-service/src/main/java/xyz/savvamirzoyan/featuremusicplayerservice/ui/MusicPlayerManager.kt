package xyz.savvamirzoyan.featuremusicplayerservice.ui

import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaBrowserCompat.SubscriptionCallback
import android.support.v4.media.MediaMetadataCompat.METADATA_KEY_MEDIA_ID
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import xyz.savvamirzoyan.featuremusicplayerservice.*
import xyz.savvamirzoyan.featuremusicplayerservice.MusicPlayerService.Companion.MEDIA_ROOT_ID
import xyz.savvamirzoyan.musicplayer.appcore.SongService
import xyz.savvamirzoyan.musicplayer.usecase_core.model.SongDomain
import javax.inject.Inject

class MusicPlayerManager @Inject constructor(
    private val musicServiceConnection: MusicServiceConnection,
    private val songDomainToSongServiceMapper: SongDomainToSongServiceMapper,
    private val scope: CoroutineScope,
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
    }

    fun skipToNextSong() {
        musicServiceConnection.transportControls.skipToNext()
    }

    fun skipToPreviousSong() {
        musicServiceConnection.transportControls.skipToPrevious()
    }

    fun seekTo(pos: Long) {
        musicServiceConnection.transportControls.seekTo(pos)
    }

    fun playOrToggleSong(mediaItems: List<SongDomain>, toggle: Boolean = false) {
        scope.launch {

            val mediaItemsService = mediaItems.map { songDomainToSongServiceMapper.map(it) }
            val mediaItem = mediaItemsService.first()

            val playbackState = musicServiceConnection.playbackStateFlow.firstOrNull()
            val currentlyPlayingSong = musicServiceConnection.currentlyPlayingSongFlow.firstOrNull()
            val isPrepared = playbackState?.isPrepared ?: false

            if (isPrepared && mediaItem.mediaId == currentlyPlayingSong?.getString(METADATA_KEY_MEDIA_ID)) {
                playbackState?.let { state ->
                    when {
                        state.isPlaying -> if (toggle) musicServiceConnection.transportControls.pause()
                        state.isPlayEnabled -> musicServiceConnection.transportControls.play()
                    }
                }
            } else {
                musicServiceConnection.transportControls.playFromMediaId(mediaItem.mediaId, null)
            }
        }
    }

    fun onClear() {
        musicServiceConnection.unsubscribe(MEDIA_ROOT_ID, object : SubscriptionCallback() {})
    }
}