package xyz.savvamirzoyan.musicplayer.appcore

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import xyz.savvamirzoyan.musicplayer.core.Model

abstract class CoreViewHolder<out V : ViewBinding, I : Model.Ui>(
    protected val binding: V
) : RecyclerView.ViewHolder(binding.root) {

    abstract fun bind(item: I)
}
