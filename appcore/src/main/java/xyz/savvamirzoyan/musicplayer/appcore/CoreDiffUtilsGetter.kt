package xyz.savvamirzoyan.musicplayer.appcore

import androidx.recyclerview.widget.DiffUtil
import xyz.savvamirzoyan.musicplayer.core.Model

interface CoreDiffUtilsGetter<T : Model.Ui> {
    fun get(old: List<T>, new: List<T>): DiffUtil.Callback
}