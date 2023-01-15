package xyz.savvamirzoyan.musicplayer.appcore

import android.widget.ImageView
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.textview.MaterialTextView
import xyz.savvamirzoyan.musicplayer.core.PictureUrl

fun ImageView.load(pictureUrl: PictureUrl?) = Glide.with(this.context)
    .load(pictureUrl)
    .transition(DrawableTransitionOptions.withCrossFade())
//    .placeholder(R.drawable.image_template)
    .dontAnimate()
    .into(this)

fun MaterialTextView.setText(textValue: TextValue) {
    val value = textValue.getString(this.context)
    isVisible = value.isNotBlank()
    text = value
}

fun CollapsingToolbarLayout.setTitle(textValue: TextValue) {
    title = textValue.getString(this.context)
}