package xyz.savvamirzoyan.featuremusicplayerservice

class MusicDatabase {
    fun getAllSongs(): List<SongService> = listOf(
        SongService(
            mediaId = "0",
            title = "Highway to Hell",
            artist = "AC/DC",
            albumUrl = "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse1.mm.bing.net%2Fth%3Fid%3DOIP.zJxHIQl6yV7Znf7FTIliLAHaHa%26pid%3DApi&f=1&ipt=f823ec4eec3bab29cdc5e8955974ee9403fa0a8fa26b555eb22d34d81069d307&ipo=images",
            songUrl = "https://cdns-preview-c.dzcdn.net/stream/c-c9dcc5dffa3210c0a7dd4d7c37f84540-3.mp3"
        ),
        SongService(
            mediaId = "1",
            title = "Ride with the Devil",
            artist = "Möntley Crüe",
            albumUrl = "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Fi.pinimg.com%2F736x%2F51%2F04%2F15%2F5104151622061fd12b3b103bbdf54734--greatest-hits-album.jpg&f=1&nofb=1&ipt=7eacaf5827fc42afc7ec3cf8b3ca110f2be2ee8ecdcd8f6992f4e10e7255a601&ipo=images",
            songUrl = "https://cdns-preview-f.dzcdn.net/stream/c-fdd3b869da59f77317e8f2365f06f2c2-3.mp3"
        )
    )
}

data class SongService(
    val mediaId: String,
    val artist: String,
    val title: String,
    val albumUrl: String,
    val songUrl: String
)
