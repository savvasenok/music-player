@file:Suppress("unused")

package xyz.savvamirzoyan.musicplayer.core

import android.util.Log

fun <T> T.logd(name: String): T {
    Log.d("SPAMEGGS", "$name:$this")
    return this
}

fun logd(string: String) = Log.d("SPAMEGGS", string)