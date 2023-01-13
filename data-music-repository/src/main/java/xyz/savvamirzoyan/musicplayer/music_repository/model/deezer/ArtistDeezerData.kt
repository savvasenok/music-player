package xyz.savvamirzoyan.musicplayer.music_repository.model.deezer


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import xyz.savvamirzoyan.musicplayer.core.ID
import xyz.savvamirzoyan.musicplayer.core.PictureUrl

@Serializable
data class ArtistDeezerData(
    val id: ID,
    val link: String,
    val name: String,
    val picture: PictureUrl,
    val radio: Boolean,
    val share: String,
    val type: String,
    @SerialName("tracklist") val trackList: PictureUrl,
    @SerialName("picture_big") val pictureBig: PictureUrl,
    @SerialName("picture_medium") val pictureMedium: PictureUrl,
    @SerialName("picture_small") val pictureSmall: PictureUrl,
    @SerialName("picture_xl") val pictureXl: PictureUrl
)