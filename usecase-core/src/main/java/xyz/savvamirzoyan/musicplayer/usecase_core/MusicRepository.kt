package xyz.savvamirzoyan.musicplayer.usecase_core

import xyz.savvamirzoyan.musicplayer.core.StringID
import xyz.savvamirzoyan.musicplayer.usecase_core.model.SongCompilationDomain
import xyz.savvamirzoyan.musicplayer.usecase_core.model.SongDomain

interface MusicRepository {

    suspend fun getSong(songId: StringID): SongDomain?
    suspend fun getSongCompilation(compilationId: StringID): SongCompilationDomain?
}
