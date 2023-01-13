package xyz.savvamirzoyan.featuremusicplayerservice

import android.support.v4.media.MediaMetadataCompat
import xyz.savvamirzoyan.musicplayer.core.mapper.Mapper
import xyz.savvamirzoyan.musicplayer.usecase_core.model.SongDomain

class SongDomainToMediaMetadataMapper : Mapper {

    fun map(model: SongDomain): MediaMetadataCompat = MediaMetadataCompat.Builder()
        .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, model.artist)
        .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, model.id)
        .putString(MediaMetadataCompat.METADATA_KEY_TITLE, model.title)
        .putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_TITLE, model.title)
        .putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_ICON_URI, model.albumPictureUrl)
        .putString(MediaMetadataCompat.METADATA_KEY_ALBUM_ART_URI, model.albumPictureUrl)
        .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_URI, model.songUrl)
        .putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_SUBTITLE, model.artist)
        .putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_DESCRIPTION, model.artist)
        .build()
}