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
}