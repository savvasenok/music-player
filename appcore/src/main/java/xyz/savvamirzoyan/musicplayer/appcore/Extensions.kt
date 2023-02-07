package xyz.savvamirzoyan.musicplayer.appcore

import android.animation.Animator
import android.os.Build
import android.view.View
import android.widget.ImageView
import android.widget.SeekBar
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView
import xyz.savvamirzoyan.musicplayer.appcore.uistate.ButtonState
import xyz.savvamirzoyan.musicplayer.appcore.uistate.TextValue
import xyz.savvamirzoyan.musicplayer.core.PictureUrl

fun ImageView.load(pictureUrl: PictureUrl?) = Glide.with(this.context)
    .load(pictureUrl)
    .transition(DrawableTransitionOptions.withCrossFade())
    .dontAnimate()
    .into(this)

fun MaterialTextView.setText(textValue: TextValue) {
    val value = textValue.getString(this.context)
    isVisible = value.isNotBlank()
    text = value
}

fun MaterialButton.setState(state: ButtonState) {
    text = state.text.getString(context)
    isEnabled = state.isEnabled
    isVisible = state.isVisible
}

fun CollapsingToolbarLayout.setTitle(textValue: TextValue) {
    title = textValue.getString(this.context)
}

fun View.hideWithAnimation() {

    if (isVisible) return

    this.isVisible = true
    this.alpha = 1f

    this.animate()
        .alpha(0f)
        .setDuration(context.resources.getInteger(android.R.integer.config_mediumAnimTime).toLong())
        .setListener(object : Animator.AnimatorListener {
            override fun onAnimationEnd(p0: Animator) {
                this@hideWithAnimation.isVisible = false
            }

            override fun onAnimationStart(p0: Animator) {}
            override fun onAnimationCancel(p0: Animator) {}
            override fun onAnimationRepeat(p0: Animator) {}
        })
        .start()
}

fun View.hideSlideDownWithAnimation() {

    if (!isVisible) return

    this.isVisible = true
    this.alpha = 1f

    this.animate()
        .alpha(0f)
        .translationY(this.height.toFloat())
        .setDuration(context.resources.getInteger(android.R.integer.config_mediumAnimTime).toLong())
        .setListener(object : Animator.AnimatorListener {
            override fun onAnimationEnd(p0: Animator) {
                this@hideSlideDownWithAnimation.isVisible = false
            }

            override fun onAnimationStart(p0: Animator) {}
            override fun onAnimationCancel(p0: Animator) {}
            override fun onAnimationRepeat(p0: Animator) {}
        })
        .start()
}

fun View.showWithAnimation() {
    if (!isVisible) return

    this.alpha = 0f
    this.isVisible = true
    this.animate()
        .alpha(1f)
        .setDuration(context.resources.getInteger(android.R.integer.config_mediumAnimTime).toLong())
        .setListener(null)
        .start()
}


fun View.showSlideUpWithAnimation() {

    if (isVisible) return

    this.alpha = 0f
    this.isVisible = true
    this.animate()
        .alpha(1f)
        .translationY(0f)
        .setDuration(context.resources.getInteger(android.R.integer.config_mediumAnimTime).toLong())
        .setListener(null)
        .start()
}

fun SeekBar.updateProgress(progress: Int) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        setProgress(progress, true)
    } else {
        this.progress = progress
    }
}