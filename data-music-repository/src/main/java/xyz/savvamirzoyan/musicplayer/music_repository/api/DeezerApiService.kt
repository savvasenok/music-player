package xyz.savvamirzoyan.musicplayer.music_repository.api

import retrofit2.http.GET
import retrofit2.http.Path
import xyz.savvamirzoyan.musicplayer.core.StringID
import xyz.savvamirzoyan.musicplayer.music_repository.model.deezer.AlbumDeezerData
import xyz.savvamirzoyan.musicplayer.music_repository.model.deezer.SongDeezerData

interface DeezerApiService {

    @GET("track/{track_id}")
    suspend fun getSong(@Path("track_id") songId: StringID): SongDeezerData?

    @GET("album/{album_id}")
    suspend fun getAlbum(@Path("album_id") albumId: StringID): AlbumDeezerData?
}