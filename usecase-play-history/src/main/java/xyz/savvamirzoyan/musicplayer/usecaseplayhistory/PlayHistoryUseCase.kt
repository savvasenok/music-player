package xyz.savvamirzoyan.musicplayer.usecaseplayhistory

import xyz.savvamirzoyan.musicplayer.usecase_core.model.SongDomain
import xyz.savvamirzoyan.musicplayer.usecaseplayhistory.model.LastPlayedPlaylistDomain
import javax.inject.Inject

interface PlayHistoryUseCase {

    suspend fun getLastPlayedSongs(): List<SongDomain>
    suspend fun getLastPlayedPlaylists(): List<LastPlayedPlaylistDomain>

    class Base @Inject constructor() : PlayHistoryUseCase {

        override suspend fun getLastPlayedSongs(): List<SongDomain> {
            return listOf(
                SongDomain(
                    id = 92719900,
                    title = "Highway to Hell",
                    artist = "AC/DC",
                    albumPictureUrl = "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse1.mm.bing.net%2Fth%3Fid%3DOIP.zJxHIQl6yV7Znf7FTIliLAHaHa%26pid%3DApi&f=1&ipt=f823ec4eec3bab29cdc5e8955974ee9403fa0a8fa26b555eb22d34d81069d307&ipo=images",
                    isExplicit = false,
                    songUrl = "https://cdns-preview-c.dzcdn.net/stream/c-c9dcc5dffa3210c0a7dd4d7c37f84540-3.mp3",
                    localCurrentPlaylistId = 0
                ),
                SongDomain(
                    id = 1637369472,
                    title = "Ride with the Devil",
                    artist = "Möntley Crüe",
                    albumPictureUrl = "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Fi.pinimg.com%2F736x%2F51%2F04%2F15%2F5104151622061fd12b3b103bbdf54734--greatest-hits-album.jpg&f=1&nofb=1&ipt=7eacaf5827fc42afc7ec3cf8b3ca110f2be2ee8ecdcd8f6992f4e10e7255a601&ipo=images",
                    isExplicit = true,
                    songUrl = "https://cdns-preview-f.dzcdn.net/stream/c-fdd3b869da59f77317e8f2365f06f2c2-3.mp3",
                    localCurrentPlaylistId = 0
                ),
                SongDomain(
                    id = 472812662,
                    title = "Комната",
                    artist = "Самое большое простое число",
                    albumPictureUrl = "https://e-cdns-images.dzcdn.net/images/cover/8f50287553296965aabb6127a3aa3a6a/120x120-000000-80-0-0.jpg",
                    isExplicit = false,
                    songUrl = "https://cdns-preview-c.dzcdn.net/stream/c-c9c3a9be3982085f3b8f199249673787-2.mp3",
                    localCurrentPlaylistId = 0
                ),
            )
        }

        override suspend fun getLastPlayedPlaylists(): List<LastPlayedPlaylistDomain> {
            return listOf(
                LastPlayedPlaylistDomain(
                    id = 0,
                    title = "Title 1 Hello there!",
                    pictureUrl = null,
                    lastPlayedTimeUTC = 23321
                ),
                LastPlayedPlaylistDomain(
                    id = 1,
                    title = "Title 2 Hello there!",
                    pictureUrl = null,
                    lastPlayedTimeUTC = 23321
                ),
                LastPlayedPlaylistDomain(
                    id = 2,
                    title = "Title 3 Hello there!",
                    pictureUrl = null,
                    lastPlayedTimeUTC = 23321
                )
            )
        }
    }
}