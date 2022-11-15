package xyz.savvamirzoyan.musicplayer.appcore

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import xyz.savvamirzoyan.musicplayer.core.mapper.Model

class CoreRecyclerViewAdapter(
    private val fingerprints: List<CoreViewHolderFingerprint<*, *>>,
    private val diffUtilCallbackGetter: CoreDiffUtilsGetter
) : RecyclerView.Adapter<CoreViewHolder<ViewBinding, Model.Ui>>() {

    private val items = mutableListOf<Model.Ui>()

    @Suppress("UNCHECKED_CAST")
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CoreViewHolder<ViewBinding, Model.Ui> {
        val inflater = LayoutInflater.from(parent.context)
        return fingerprints.find { it.getLayoutRes() == viewType }
            ?.getViewHolder(inflater, parent)
            ?.let { it as CoreViewHolder<ViewBinding, Model.Ui> }
            ?: throw  IllegalArgumentException("Illegal viewtype: $viewType")
    }

    override fun onBindViewHolder(
        holder: CoreViewHolder<ViewBinding, Model.Ui>,
        position: Int
    ) = holder.bind(items[position])

    override fun getItemCount() = items.size

    fun update(newItems: List<Model.Ui>) {
        val diffCallback = diffUtilCallbackGetter.get(old = items, new = newItems)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        items.clear()
        items.addAll(newItems)
        diffResult.dispatchUpdatesTo(this)
    }
}