@file:Suppress("unused")

package xyz.savvamirzoyan.musicplayer.usecaseplayhistory

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
interface UseCasePlayHistoryHiltModule {

    @Singleton
    @Binds
    fun bindPlayHistoryUseCase(base: PlayHistoryUseCase.Base): PlayHistoryUseCase
}