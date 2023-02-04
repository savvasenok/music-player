package xyz.savvamirzoyan.musicplayer.appcore

import xyz.savvamirzoyan.musicplayer.appcore.uistate.TextValue
import xyz.savvamirzoyan.musicplayer.core.Model
import xyz.savvamirzoyan.musicplayer.core.PictureUrl
import xyz.savvamirzoyan.musicplayer.core.StringID

data class SongUi(
    val id: StringID,
    val albumId: StringID,
    val title: TextValue,
    val artist: TextValue,
    val albumPictureUrl: PictureUrl,
    val isExplicit: Boolean,
    val onClickListener: (songId: StringID) -> Unit,
    val isPlaying: Boolean
) : Model.Ui {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is SongUi) return false

        if (id != other.id) return false
        if (albumId != other.albumId) return false
        if (title != other.title) return false
        if (artist != other.artist) return false
        if (albumPictureUrl != other.albumPictureUrl) return false
        if (isExplicit != other.isExplicit) return false
        if (isPlaying != other.isPlaying) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + albumId.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + artist.hashCode()
        result = 31 * result + albumPictureUrl.hashCode()
        result = 31 * result + isExplicit.hashCode()
        result = 31 * result + isPlaying.hashCode()
        return result
    }
}