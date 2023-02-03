package xyz.savvamirzoyan.musicplayer.appcore.widget

import android.content.Context
import android.graphics.drawable.Animatable2
import android.graphics.drawable.AnimatedVectorDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import androidx.core.view.isVisible
import xyz.savvamirzoyan.musicplayer.appcore.databinding.LayoutPlayableImageViewBinding
import xyz.savvamirzoyan.musicplayer.appcore.hideWithAnimation
import xyz.savvamirzoyan.musicplayer.appcore.showWithAnimation

class PlayableImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {

    private val binding = LayoutPlayableImageViewBinding.inflate(LayoutInflater.from(context), this, true)
    private val animDrawable = binding.ivPlayingBarsAnimation.drawable as AnimatedVectorDrawable

    val image = binding.ivPicture

    init {
        animDrawable.registerAnimationCallback(object : Animatable2.AnimationCallback() {
            override fun onAnimationEnd(drawable: Drawable?) {
                animDrawable.start()
            }
        })
        animDrawable.start()
    }

    private var isCurrentlyPlaying: Boolean = false
    fun setIsPlaying(isPlaying: Boolean) {

        if (isCurrentlyPlaying == isPlaying) return
        isCurrentlyPlaying = isPlaying

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