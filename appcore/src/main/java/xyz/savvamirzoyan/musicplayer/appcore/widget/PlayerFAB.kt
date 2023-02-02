package xyz.savvamirzoyan.musicplayer.appcore.widget

import android.animation.ObjectAnimator
import android.content.Context
import android.util.AttributeSet
import com.google.android.material.floatingactionbutton.FloatingActionButton
import xyz.savvamirzoyan.musicplayer.appcore.R

class PlayerFAB @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FloatingActionButton(context, attrs, defStyleAttr) {

    private val dp = context.resources.displayMetrics.density
    private val isPlayingCornerRadius = context.resources
        .getDimension(R.dimen.playerFAB_corner_radius_is_playing)
        .times(dp)
    private val isPausedCornerRadius = context.resources
        .getDimension(R.dimen.playerFAB_corner_radius_is_paused)
        .times(dp)

    private val toIsPlayingStateAnimationDuration: Long =
        context.resources.getInteger(R.integer.duration_to_is_playing_state).toLong()
    private val toIsPausedStateAnimationDuration: Long =
        context.resources.getInteger(R.integer.duration_to_is_paused_state).toLong()

    init {
        shapeAppearanceModel = shapeAppearanceModel.withCornerSize(isPausedCornerRadius)
    }

    fun toggle(isPlaying: Boolean) {
        if (isPlaying) {
            animateShapeAppearance(isPausedCornerRadius, isPlayingCornerRadius, toIsPausedStateAnimationDuration)
            setImageResource(R.drawable.ic_player_pause)
        } else {
            animateShapeAppearance(isPlayingCornerRadius, isPausedCornerRadius, toIsPlayingStateAnimationDuration)
            setImageResource(R.drawable.ic_player_play)
        }
    }

    private fun animateShapeAppearance(start: Float, end: Float, time: Long) {
        ObjectAnimator.ofFloat(start, end)
            .apply {
                duration = time
                addUpdateListener {
                    shapeAppearanceModel = shapeAppearanceModel.withCornerSize(it.animatedValue as Float)
                }
            }.start()
    }
}