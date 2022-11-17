package xyz.savvamirzoyan.musicplayer.usecasedatetime

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
interface UseCaseDatetimeHiltModule {

    @Binds
    fun bindUseCaseDatetime(base: DateTimeUseCase.Base): DateTimeUseCase
}