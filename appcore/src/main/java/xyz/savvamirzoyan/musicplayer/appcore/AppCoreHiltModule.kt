package xyz.savvamirzoyan.musicplayer.appcore

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import xyz.savvamirzoyan.musicplayer.appcore.mapper.TimeToStringMapper
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class AppCoreHiltModule {

    @Singleton
    @Binds
    abstract fun bindTimeToStringMapper(base: TimeToStringMapper.Base): TimeToStringMapper

    companion object {
        @Singleton
        @Provides
        fun provideGlideInstance(@ApplicationContext ctx: Context) = Glide.with(ctx)
            .setDefaultRequestOptions(
                RequestOptions()
                    .placeholder(R.drawable.image_template)
                    .error(R.drawable.ic_no_image)
                    .diskCacheStrategy(DiskCacheStrategy.DATA)
            )
    }
}