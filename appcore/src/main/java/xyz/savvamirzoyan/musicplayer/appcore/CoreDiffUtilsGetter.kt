package xyz.savvamirzoyan.musicplayer.appcore

import androidx.recyclerview.widget.DiffUtil
import xyz.savvamirzoyan.musicplayer.core.mapper.Model

interface CoreDiffUtilsGetter {
    fun get(old: List<Model.Ui>, new: List<Model.Ui>): DiffUtil.Callback
}