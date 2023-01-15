package xyz.savvamirzoyan.musicplayer.appcore

import android.content.Context
import androidx.annotation.StringRes

class TextValue private constructor() {

    private var stringValue: String? = null

    @StringRes
    private var stringId: Int? = null
    private var stringArgs: Array<Any> = emptyArray()

    constructor(string: String) : this() {
        stringValue = string
    }

    constructor(@StringRes stringId: Int) : this() {
        this.stringId = stringId
    }

    constructor(@StringRes stringId: Int, args: Array<Any>) : this() {
        this.stringId = stringId
        this.stringArgs = args
    }

    fun getString(context: Context): String =
        stringValue ?: stringId?.let { context.getString(it, *stringArgs) } ?: ""
}
