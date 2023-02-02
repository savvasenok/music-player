package xyz.savvamirzoyan.featuremusicplayerservice.ui

import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaBrowserCompat.SubscriptionCallback
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import xyz.savvamirzoyan.featuremusicplayerservice.MusicPlayerService.Companion.MEDIA_ROOT_ID
import xyz.savvamirzoyan.featuremusicplayerservice.MusicServiceConnection
import xyz.savvamirzoyan.musicplayer.appcore.SongService
import xyz.savvamirzoyan.musicplayer.core.StringID
import xyz.savvamirzoyan.musicplayer.usecaseplayermanager.UseCaseMusicPlayerManager
import javax.inject.Inject

// class to manage player from local UI (buttons, seekbars etc)
class MusicPlayerManager @Inject constructor(
    private val musicPlayerManagerUseCase: UseCaseMusicPlayerManager,
    private val musicServiceConnection: MusicServiceConnection,
    scope: CoroutineScope
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

        scope.launch {

            musicPlayerManagerUseCase.currentUserSelectedSongFlow.collect {
                it?.also { playSong(it.id) }
            }
        }
    }

    @Suppress("unused")
    fun skipToNextSong() {
        musicServiceConnection.transportControls.skipToNext()
    }

    @Suppress("unused")
    fun skipToPreviousSong() {
        musicServiceConnection.transportControls.skipToPrevious()
    }

    @Suppress("unused")
    fun seekTo(pos: Long) {
        musicServiceConnection.transportControls.seekTo(pos)
    }

    //    fun playOrToggleSongs(songs: List<SongDomain>, toggle: Boolean = false) {
//        scope.launch {
//
//            val songsService = songs.map { songDomainToSongServiceMapper.map(it) }
//            val songService = songsService.first()
//
//            val playbackState = musicServiceConnection.playbackStateFlow.firstOrNull()
//            val currentlyPlayingSong = musicPlayerManagerUseCase.currentUserSelectedSongFlow.firstOrNull()
//            val isPrepared = playbackState?.isPrepared ?: false
//
//             // If player is already has some music to play (paused or playing)
//            if (isPrepared) {
//
//            } else {
//
//            }
//
//            if (isPrepared && songService.mediaId == currentlyPlayingSong?.id) {
//                playbackState?.let { state ->
//                    when {
//                        state.isPlaying -> if (toggle) musicServiceConnection.transportControls.pause()
//                        state.isPlayEnabled -> musicServiceConnection.transportControls.play()
//                    }
//                }
//            } else {
//                musicServiceConnection.transportControls.playFromMediaId(songService.mediaId, null)
//            }
//        }
//    }
//
    private fun playSong(songID: StringID) {
        musicServiceConnection.transportControls.playFromMediaId(songID, null)
    }

    fun onClear() {
        musicServiceConnection.unsubscribe(MEDIA_ROOT_ID, object : SubscriptionCallback() {})
    }
}