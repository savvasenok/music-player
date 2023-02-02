package xyz.savvamirzoyan.musicplayer.feature_songs_list

import android.graphics.drawable.Animatable2
import android.graphics.drawable.AnimatedVectorDrawable
import android.graphics.drawable.Drawable
import androidx.core.view.isVisible
import xyz.savvamirzoyan.musicplayer.appcore.*
import xyz.savvamirzoyan.musicplayer.core.Model
import xyz.savvamirzoyan.musicplayer.feature_songs_list.databinding.LayoutSongRowBinding

class SongRowViewHolder(
    binding: LayoutSongRowBinding
) : CoreViewHolder<LayoutSongRowBinding, SongUi>(binding) {

    val animDrawable = binding.ivPlayingBarsAnimation.drawable as AnimatedVectorDrawable

    init {
        animDrawable.registerAnimationCallback(object : Animatable2.AnimationCallback() {
            override fun onAnimationEnd(drawable: Drawable?) {
                animDrawable.start()
            }
        })
        animDrawable.start()
    }

    override fun bind(item: SongUi) {
        binding.ivAlbumPicture.load(item.albumPictureUrl)
        binding.tvSongTitle.setText(item.title)
        binding.tvArtistName.setText(item.artist)
        binding.ivIsExplicit.isVisible = item.isExplicit

        binding.root.setOnClickListener { item.onClickListener(item.id) }

        if (item.isPlaying) setPlayingStatus(true)
    }

    override fun <R : Model.Ui> bindPayload(item: SongUi, payload: List<R>) {
        payload.forEach {
            when (it) {
                is SongPlayingStatusPayload -> setPlayingStatus(it.isPlaying)
            }
        }
    }

    private fun setPlayingStatus(isPlaying: Boolean) {
        binding.ivPlayingBarsAnimation.isVisible = isPlaying
        binding.ivOverlayDark.isVisible = isPlaying

        when (isPlaying) {
            true -> {
                binding.ivPlayingBarsAnimation.showWithAnimation()
                binding.ivOverlayDark.showWithAnimation()
            }
            false -> {
                binding.ivPlayingBarsAnimation.hideWithAnimation()
                binding.ivOverlayDark.hideWithAnimation()
            }
        }
    }
}