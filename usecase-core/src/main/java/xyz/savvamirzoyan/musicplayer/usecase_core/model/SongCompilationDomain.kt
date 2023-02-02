package xyz.savvamirzoyan.musicplayer.usecase_core.model

import xyz.savvamirzoyan.musicplayer.core.Model
import xyz.savvamirzoyan.musicplayer.core.PictureUrl
import xyz.savvamirzoyan.musicplayer.core.StringID

sealed class SongCompilationDomain(
    val id: StringID,
    val songs: List<SongDomain>
) {

    class AlbumDomain(
        id: StringID,
        val title: String,
        val coverPictureUrl: PictureUrl,
        val authorName: String,
        val authorPictureUrl: PictureUrl?,
        val description: String?,
        val fans: Int,
        songs: List<SongDomain>,
        val compilationId: String? = null
    ) : SongCompilationDomain(id, songs), Model.Domain {

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is AlbumDomain) return false

            if (title != other.title) return false
            if (coverPictureUrl != other.coverPictureUrl) return false
            if (authorName != other.authorName) return false
            if (authorPictureUrl != other.authorPictureUrl) return false
            if (description != other.description) return false
            if (fans != other.fans) return false
            if (compilationId != other.compilationId) return false

            return true
        }

        override fun hashCode(): Int {
            var result = id.hashCode()
            result = 31 * result + title.hashCode()
            result = 31 * result + coverPictureUrl.hashCode()
            result = 31 * result + authorName.hashCode()
            result = 31 * result + (authorPictureUrl?.hashCode() ?: 0)
            result = 31 * result + (description?.hashCode() ?: 0)
            result = 31 * result + fans
            result = 31 * result + compilationId.hashCode()
            return result
        }

        override fun toString(): String {
            return "AlbumDomain(title='$title', coverPictureUrl='$coverPictureUrl', authorName='$authorName', authorPictureUrl=$authorPictureUrl, description=$description, fans=$fans)"
        }
    }

    class PlaylistDomain(
        id: StringID,
        val title: String,
        val coverPictureUrl: PictureUrl,
        val authorName: String,
        val authorPictureUrl: PictureUrl?,
        val description: String?,
        songs: List<SongDomain>,
        val compilationId: String? = null
    ) : SongCompilationDomain(id, songs), Model.Domain {


        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is PlaylistDomain) return false

            if (id != other.id) return false
            if (title != other.title) return false
            if (coverPictureUrl != other.coverPictureUrl) return false
            if (authorName != other.authorName) return false
            if (authorPictureUrl != other.authorPictureUrl) return false
            if (description != other.description) return false
            if (songs != other.songs) return false
            if (compilationId != other.compilationId) return false

            return true
        }

        override fun hashCode(): Int {
            var result = id.hashCode()
            result = 31 * result + title.hashCode()
            result = 31 * result + coverPictureUrl.hashCode()
            result = 31 * result + authorName.hashCode()
            result = 31 * result + (authorPictureUrl?.hashCode() ?: 0)
            result = 31 * result + (description?.hashCode() ?: 0)
            result = 31 * result + (compilationId?.hashCode() ?: 0)
            return result
        }
    }
}