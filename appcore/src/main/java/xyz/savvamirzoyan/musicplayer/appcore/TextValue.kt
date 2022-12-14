package xyz.savvamirzoyan.musicplayer.appcore

import android.content.Context
import androidx.annotation.StringRes
import xyz.savvamirzoyan.musicplayer.core.Model

sealed interface TextValue : Model.Ui {

    fun get(context: Context): String

    data class AsResource(
        @StringRes val resourceId: Int,
        val arguments: List<Any> = emptyList()
    ) : TextValue {
        override fun get(context: Context): String = context.getString(resourceId, *arguments.toTypedArray())
    }

    data class AsString(
        val value: String
    ) : TextValue {
        override fun get(context: Context) = value
    }
}