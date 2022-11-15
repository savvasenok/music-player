package xyz.savvamirzoyan.musicplayer.appcore

import android.content.Context
import androidx.annotation.StringRes
import xyz.savvamirzoyan.musicplayer.core.mapper.Model

sealed interface TextValue : Model.Ui {

    fun get(context: Context): String

    data class AsResource(
        @StringRes val resourceId: Int,
        val arguments: List<Any>
    ) : TextValue {
        override fun get(context: Context): String = context.getString(resourceId, arguments)
    }

    data class AsString(
        val value: String
    ) : TextValue {
        override fun get(context: Context) = value
    }
}