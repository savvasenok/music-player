package xyz.savvamirzoyan.musicplayer.usecaseplayhistory

import xyz.savvamirzoyan.musicplayer.usecaseplayhistory.model.LastPlayedPlaylistDomain
import xyz.savvamirzoyan.musicplayer.usecaseplayhistory.model.LastPlayedSongDomain
import javax.inject.Inject

interface PlayHistoryUseCase {

    suspend fun getLastPlayedSongs(): List<LastPlayedSongDomain>
    suspend fun getLastPlayedPlaylists(): List<LastPlayedPlaylistDomain>

    class Base @Inject constructor() : PlayHistoryUseCase {

        override suspend fun getLastPlayedSongs(): List<LastPlayedSongDomain> {
            return listOf(
                LastPlayedSongDomain(
                    id = 0,
                    title = "Highway to Hell",
                    artist = "AC/DC",
                    albumPictureUrl = "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse1.mm.bing.net%2Fth%3Fid%3DOIP.zJxHIQl6yV7Znf7FTIliLAHaHa%26pid%3DApi&f=1&ipt=f823ec4eec3bab29cdc5e8955974ee9403fa0a8fa26b555eb22d34d81069d307&ipo=images",
                    isExplicit = false
                ),

                LastPlayedSongDomain(
                    id = 1,
                    title = "Ride with the Devil",
                    artist = "Möntley Crüe",
                    albumPictureUrl = "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Fi.pinimg.com%2F736x%2F51%2F04%2F15%2F5104151622061fd12b3b103bbdf54734--greatest-hits-album.jpg&f=1&nofb=1&ipt=7eacaf5827fc42afc7ec3cf8b3ca110f2be2ee8ecdcd8f6992f4e10e7255a601&ipo=images",
                    isExplicit = true
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