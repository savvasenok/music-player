package xyz.savvamirzoyan.musicplayer.usecaseplayermanager

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
interface UseCaseMusicPlayerManagerHiltModule {

    @Singleton
    @Binds
    fun bindMusicPlayerManager(base: UseCaseMusicPlayerManager.Base): UseCaseMusicPlayerManager
}