package xyz.savvamirzoyan.featuremusicplayerservice

import android.content.Context
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.audio.AudioAttributes
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ServiceScoped
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import javax.inject.Singleton

@InstallIn(ServiceComponent::class)
@Module
abstract class ServiceHiltModule {

    companion object {
        @ServiceScoped
        @Provides
        fun provideMusicDatabase() = MusicDatabase()

        @ServiceScoped
        @Provides
        fun provideAudioAttributes() = AudioAttributes.Builder()
            .setContentType(C.AUDIO_CONTENT_TYPE_MUSIC)
            .setUsage(C.USAGE_MEDIA)
            .build()

        @ServiceScoped
        @Provides
        fun provideExoPlayer(
            @ApplicationContext context: Context,
            audioAttributes: AudioAttributes
        ) = ExoPlayer.Builder(context).build().apply {
            setAudioAttributes(audioAttributes, true)
            setHandleAudioBecomingNoisy(true)
        }

        @ServiceScoped
        @Provides
        fun provideDataSourceFactory(
            @ApplicationContext context: Context
        ) = DefaultDataSourceFactory(context, Util.getUserAgent(context, "Spotify App"))

    }
}

@InstallIn(SingletonComponent::class)
@Module
abstract class ApplicationHiltModule {

    @Singleton
    @Binds
    abstract fun bindSongDomainToSongServiceMapper(base: SongDomainToSongServiceMapper.Base): SongDomainToSongServiceMapper

    companion object {

        @Singleton
        @Provides
        fun provideMusicServiceConnection(
            @ApplicationContext context: Context,
            scope: CoroutineScope
        ) = MusicServiceConnection(context, scope)

        @Singleton
        @Provides
        fun provideCoroutineScope() = CoroutineScope(Dispatchers.Main + Job())
    }
}