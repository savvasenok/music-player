package xyz.savvamirzoyan.musicplayer.music_repository.model.deezer


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import xyz.savvamirzoyan.musicplayer.core.ID

@Serializable
data class ContributorDeezerData(
    val id: ID,
    val link: String,
    val name: String,
    val picture: String,
    val radio: Boolean,
    val role: String,
    val share: String,
    val type: String,
    @SerialName("tracklist")val trackList: String,
    @SerialName("picture_xl") val pictureXl: String,
    @SerialName("picture_big") val pictureBig: String,
    @SerialName("picture_medium") val pictureMedium: String,
    @SerialName("picture_small") val pictureSmall: String
)