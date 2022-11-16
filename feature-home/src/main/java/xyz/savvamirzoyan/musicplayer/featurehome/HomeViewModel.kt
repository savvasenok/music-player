package xyz.savvamirzoyan.musicplayer.featurehome

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import xyz.savvamirzoyan.musicplayer.appcore.CoreViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : CoreViewModel() {

    private val _playlistsStateFlow = flow<LastPlaylistsStateUi> {
        emit(
            LastPlaylistsStateUi(
                LastPlaylistStateUi(
                    pictureUrl = "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Fcdn.musebycl.io%2F2020-07%2Fart-of-the-album-van-halen-hed-2020.jpg&f=1&nofb=1&ipt=6d3dad10007a161796010855d833e90e7b2627d1605e1671f278aef6aca12c8b&ipo=images",
                    title = "Van Halen",
                    isPlaying = false
                ),
                LastPlaylistStateUi(
                    pictureUrl = "https://cdn.musebycl.io/2020-07/Queen.jpg",
                    title = "News of the World - Queen",
                    isPlaying = false
                ),
                LastPlaylistStateUi(
                    pictureUrl = "https://cdn.musebycl.io/2020-07/ACDC.jpg",
                    title = "Highway to Hell - AC/DC",
                    isPlaying = true
                ),
                LastPlaylistStateUi(
                    pictureUrl = "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Fcdn-images-1.medium.com%2Fmax%2F1200%2F1*8FkvzbSdSJ4HNxtuZo5kLg.jpeg&f=1&nofb=1&ipt=5e0e513cb585da6fc885c2220609201dc6d9e187a39c45e2f59008320d26a1ff&ipo=images",
                    title = "The Dark side of the Moon - Pink Floyd",
                    isPlaying = false
                )
            )
        )
        delay(5000)
    }//MutableStateFlow(LastPlaylistsStateUi())
    internal val playlistsStateFlow: Flow<LastPlaylistsStateUi> = _playlistsStateFlow
}