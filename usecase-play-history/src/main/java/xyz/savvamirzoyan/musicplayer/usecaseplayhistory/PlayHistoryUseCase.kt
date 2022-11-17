package xyz.savvamirzoyan.musicplayer.usecaseplayhistory

import javax.inject.Inject

interface PlayHistoryUseCase {

    suspend fun getLastPlayedPlaylists(): List<LastPlayedPlaylistDomain>

    class Base @Inject constructor() : PlayHistoryUseCase {

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