package xyz.savvamirzoyan.featuremusicplayerservice.ui

import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaBrowserCompat.SubscriptionCallback
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import xyz.savvamirzoyan.featuremusicplayerservice.MusicPlayerService.Companion.MEDIA_ROOT_ID
import xyz.savvamirzoyan.featuremusicplayerservice.MusicServiceConnection
import xyz.savvamirzoyan.featuremusicplayerservice.SongDomainToSongServiceMapper
import xyz.savvamirzoyan.featuremusicplayerservice.SongService
import xyz.savvamirzoyan.featuremusicplayerservice.isPrepared
import xyz.savvamirzoyan.musicplayer.usecaseplayermanager.UseCaseMusicPlayerManager
import javax.inject.Inject

class MusicPlayerManager @Inject constructor(
    private val musicServiceConnection: MusicServiceConnection,
    private val musicPlayerManagerUseCase: UseCaseMusicPlayerManager,
    private val songDomainToSongServiceMapper: SongDomainToSongServiceMapper,
    private val scope: CoroutineScope,
) {

    val mediaItemsFlow = musicPlayerManagerUseCase.songsPlaylistFlow
        .map { list -> list.map { songDomainToSongServiceMapper.map(it) } }

//    val isConnectedFlow: Flow<Boolean> = musicServiceConnection.isConnectedFlow
//    val isNetworkErrorFlow: Flow<Boolean> = musicServiceConnection.networkErrorFlow
//    private val currentlyPlayingSongFlow: Flow<MediaMetadataCompat> =
//        musicServiceConnection.currentlyPlayingSongFlow.filterNotNull()
//    private val playbackStateFlow: Flow<PlaybackStateCompat?> = musicServiceConnection.playbackStateFlow

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

    fun playOrToggleSong(mediaItem: SongService, toggle: Boolean = false) {
        scope.launch {

            Log.d("SPAMEGGS", "playOrToggleSong started coroutine")

            val playbackState = null //playbackStateFlow.firstOrNull()
            val currentlyPlayingSong = null //currentlyPlayingSongFlow.firstOrNull()
            val isPrepared = playbackState?.isPrepared ?: false

            Log.d("SPAMEGGS", "before if statement")

//            if (isPrepared && mediaItem.mediaId == currentlyPlayingSong?.getString(METADATA_KEY_MEDIA_ID)) {
//                playbackState?.let { state ->
//                    when {
//                        state.isPlaying -> if (toggle) musicServiceConnection.transportControls.pause()
//                        state.isPlayEnabled -> musicServiceConnection.transportControls.play()
//                        else -> {}
//                    }
//                }
//            } else {
//                Log.d("SPAMEGGS", "playFromMediaId(${mediaItem.mediaId})")
            musicServiceConnection.transportControls.playFromMediaId(mediaItem.mediaId, null)
//            }
        }
    }

    fun onClear() {
        musicServiceConnection.unsubscribe(MEDIA_ROOT_ID, object : SubscriptionCallback() {})
    }
}