package xyz.savvamirzoyan.featuremusicplayerservice

import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaBrowserCompat.MediaItem.FLAG_PLAYABLE
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.MediaMetadataCompat.*
import android.util.Log
import androidx.core.net.toUri
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.source.ConcatenatingMediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import xyz.savvamirzoyan.featuremusicplayerservice.State.*
import javax.inject.Inject

class DeezerMusicSource @Inject constructor(private val musicDatabase: MusicDatabase) {

    var songs = emptyList<MediaMetadataCompat>()

    suspend fun fetchMediaData() = withContext(Dispatchers.IO) {
        state = STATE_INITIALIZING
        songs = musicDatabase.getAllSongs()
            .map {
                Builder()
                    .putString(METADATA_KEY_ARTIST, it.artist)
                    .putString(METADATA_KEY_MEDIA_ID, it.mediaId)
                    .putString(METADATA_KEY_TITLE, it.title)
                    .putString(METADATA_KEY_DISPLAY_TITLE, it.title)
                    .putString(METADATA_KEY_DISPLAY_ICON_URI, it.albumUrl)
                    .putString(METADATA_KEY_ALBUM_ART_URI, it.albumUrl)
                    .putString(METADATA_KEY_MEDIA_URI, it.songUrl)
                    .putString(METADATA_KEY_DISPLAY_SUBTITLE, it.artist)
                    .putString(METADATA_KEY_DISPLAY_DESCRIPTION, it.artist)
                    .build()
            }
        state = STATE_INITIALIZED
    }

//    fun asMediaSource(dataSourceFactory: DefaultDataSourceFactory): ConcatenatingMediaSource {
    fun asMediaSource(song: MediaMetadataCompat, dataSourceFactory: DefaultDataSourceFactory): ConcatenatingMediaSource {

    Log.d("SPAMEGGS", "asMediaSource(song:$song)")

        val concatenatingMediaSource = ConcatenatingMediaSource()
//        songs.forEach { song ->
            val mediaItem = MediaItem.fromUri(song.getString(METADATA_KEY_MEDIA_URI).toUri())
            val mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(mediaItem)
            concatenatingMediaSource.addMediaSource(mediaSource)
//        }

        return concatenatingMediaSource
    }

    fun asMediaItems() = songs.map { song ->
        val desc = MediaDescriptionCompat.Builder()
            .setMediaUri(song.getString(METADATA_KEY_MEDIA_URI).toUri())
            .setTitle(song.description.title)
            .setSubtitle(song.description.subtitle)
            .setMediaId(song.description.mediaId)
            .setIconUri(song.description.iconUri)
            .build()
        MediaBrowserCompat.MediaItem(desc, FLAG_PLAYABLE)
    }.toMutableList()

    private val onReadyListeners = mutableListOf<(Boolean) -> Unit>()

    private var state: State = STATE_INITIALIZED
        set(value) {
            if (value == STATE_INITIALIZED || value == STATE_ERROR) {
                synchronized(onReadyListeners) {
                    field = value
                    onReadyListeners.forEach { listener ->
                        listener(state == STATE_INITIALIZED)
                    }
                }
            } else {
                field = value
            }
        }

    fun whenReady(action: (Boolean) -> Unit): Boolean = if (state == STATE_CREATED || state == STATE_INITIALIZING) {
        onReadyListeners += action
        false
    } else {
        action(state == STATE_INITIALIZED)
        true
    }
}

enum class State {
    STATE_CREATED, STATE_INITIALIZING, STATE_INITIALIZED, STATE_ERROR
}