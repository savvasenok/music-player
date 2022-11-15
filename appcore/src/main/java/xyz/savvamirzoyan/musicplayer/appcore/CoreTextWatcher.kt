package xyz.savvamirzoyan.musicplayer.appcore

import android.text.Editable
import android.text.TextWatcher

abstract class CoreTextWatcher : TextWatcher {

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

    override fun afterTextChanged(p0: Editable?) {
        onChange(p0?.toString() ?: "")
    }

    abstract fun onChange(value: String)
}