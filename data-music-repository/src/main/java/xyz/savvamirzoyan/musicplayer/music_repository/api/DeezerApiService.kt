package xyz.savvamirzoyan.musicplayer.music_repository.api

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import xyz.savvamirzoyan.musicplayer.core.ID
import xyz.savvamirzoyan.musicplayer.music_repository.model.deezer.SongDeezerData

interface DeezerApiService {

    @GET("track/{track_id}")
    suspend fun getSong(@Path("track_id") songId: ID): SongDeezerData?


}