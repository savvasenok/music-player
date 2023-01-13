package xyz.savvamirzoyan.musicplayer.music_repository.model.deezer


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import xyz.savvamirzoyan.musicplayer.core.SongUrl

@Serializable
data class SongDeezerData(
    val album: AlbumDeezerData,
    val artist: ArtistDeezerData,
    val bpm: Double,
    val contributors: List<ContributorDeezerData>,
    val duration: Int,
    val gain: Double,
    val id: Int,
    val isrc: String,
    val link: String,
    val preview: SongUrl,
    val rank: Int,
    val readable: Boolean, // true if the track is readable in the player for the current user
    val share: String,
    val title: String,
    val type: String,
    @SerialName("available_countries") val availableCountries: List<String>,
    @SerialName("disk_number") val diskNumber: Int,
    @SerialName("explicit_content_cover") val explicitContentCover: Int,
    @SerialName("explicit_content_lyrics") val explicitContentLyrics: Int,
    @SerialName("explicit_lyrics") val explicitLyrics: Boolean,
    @SerialName("md5_image") val md5Image: String,
    @SerialName("release_date") val releaseDate: String,
    @SerialName("title_short") val titleShort: String,
    @SerialName("title_version") val titleVersion: String,
    @SerialName("track_position") val trackPosition: Int
)