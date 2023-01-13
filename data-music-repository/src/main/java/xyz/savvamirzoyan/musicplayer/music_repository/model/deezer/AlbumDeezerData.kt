package xyz.savvamirzoyan.musicplayer.music_repository.model.deezer


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import xyz.savvamirzoyan.musicplayer.core.ID
import xyz.savvamirzoyan.musicplayer.core.PictureUrl

@Serializable
data class AlbumDeezerData(
    val cover: String,
    val id: ID,
    val link: String,
    val title: String,
    val type: String,
    @SerialName("tracklist") val trackList: String,
    @SerialName("cover_big") val coverBig: PictureUrl,
    @SerialName("cover_medium") val coverMedium: PictureUrl,
    @SerialName("cover_small") val coverSmall: PictureUrl,
    @SerialName("cover_xl") val coverXl: PictureUrl,
    @SerialName("md5_image") val md5Image: String,
    @SerialName("release_date") val releaseDate: String
)