package xyz.savvamirzoyan.musicplayer.music_repository.model.deezer

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import xyz.savvamirzoyan.musicplayer.core.ID
import xyz.savvamirzoyan.musicplayer.core.PictureUrl

@Serializable
data class ArtistDeezerData(
    val id: ID,
    val link: String? = null,
    val name: String,
    val picture: PictureUrl? = null,
    val radio: Boolean? = null,
    val share: String? = null,
    val type: String,
    @SerialName("tracklist") val trackList: PictureUrl,
    @SerialName("picture_big") val pictureBig: PictureUrl? = null,
    @SerialName("picture_medium") val pictureMedium: PictureUrl? = null,
    @SerialName("picture_small") val pictureSmall: PictureUrl? = null,
    @SerialName("picture_xl") val pictureXl: PictureUrl? = null
)