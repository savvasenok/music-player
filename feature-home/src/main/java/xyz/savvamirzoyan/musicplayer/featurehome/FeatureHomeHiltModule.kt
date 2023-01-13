package xyz.savvamirzoyan.musicplayer.featurehome

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
interface FeatureHomeHiltModule {

    @Binds
    fun bindCurrentPartOfDayToTextValueMapper(base: CurrentPartOfDayToTextValueMapper.Base): CurrentPartOfDayToTextValueMapper

    @Binds
    fun bindLastPlayedPlaylistToLastPlaylistsStateMapper(base: LastPlayedPlaylistToLastPlaylistsStateMapper.Base): LastPlayedPlaylistToLastPlaylistsStateMapper

    @Binds
    fun bindLastPlayedSongDomainToUiMapper(base: SongDomainToUiMapper.Base): SongDomainToUiMapper

    @Binds
    fun bindSongDomainToSongMapper(base: SongDomainToSongMapper.Base): SongDomainToSongMapper
}