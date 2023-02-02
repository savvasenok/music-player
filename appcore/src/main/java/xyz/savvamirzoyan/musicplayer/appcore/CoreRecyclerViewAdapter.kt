package xyz.savvamirzoyan.musicplayer.appcore

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import xyz.savvamirzoyan.musicplayer.core.Model

class CoreRecyclerViewAdapter<T : Model.Ui>(
    private val fingerprints: List<CoreViewHolderFingerprint<*, *>>,
    private val diffUtilCallbackGetter: CoreDiffUtilsGetter<T>
) : RecyclerView.Adapter<CoreViewHolder<ViewBinding, Model.Ui>>() {

    private var items = listOf<T>()

    @Suppress("UNCHECKED_CAST")
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CoreViewHolder<ViewBinding, Model.Ui> {
        val inflater = LayoutInflater.from(parent.context)
        return fingerprints.find { it.getLayoutRes() == viewType }
            ?.getViewHolder(inflater, parent)
            ?.let { it as CoreViewHolder<ViewBinding, Model.Ui> }
            ?: throw  IllegalArgumentException("Illegal view type: $viewType")
    }

    override fun onBindViewHolder(
        holder: CoreViewHolder<ViewBinding, Model.Ui>,
        position: Int
    ) = holder.bind(items[position])

    override fun onBindViewHolder(
        holder: CoreViewHolder<ViewBinding, Model.Ui>,
        position: Int,
        payloads: MutableList<Any>
    ) {
        @Suppress("UNCHECKED_CAST")
        if (payloads.isNotEmpty()) holder.bindPayload(items[position], payloads as List<Model.Ui>)
        else super.onBindViewHolder(holder, position, payloads)
    }

    override fun getItemCount() = items.size

    override fun getItemViewType(position: Int): Int {
        val item = items[position]
        return fingerprints.find { it.isRelativeItem(item) }
            ?.getLayoutRes()
            ?: throw IllegalArgumentException()
    }

    fun update(newItems: List<T>) {
        val diffCallback = diffUtilCallbackGetter.get(old = items, new = newItems)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        items = newItems
        diffResult.dispatchUpdatesTo(this)
    }
}