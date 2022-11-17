package xyz.savvamirzoyan.musicplayer.appcore

import android.widget.ImageView
import com.bumptech.glide.Glide
import xyz.savvamirzoyan.musicplayer.core.PictureUrl

fun ImageView.load(pictureUrl: PictureUrl?) = Glide.with(this.context)
    .load(pictureUrl)
    .placeholder(R.drawable.image_template)
    .dontAnimate()
    .into(this)