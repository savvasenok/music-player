package xyz.savvamirzoyan.musicplayer.featurehome

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import xyz.savvamirzoyan.musicplayer.appcore.CoreItemDecoration
import xyz.savvamirzoyan.musicplayer.featurehome.model.LastPlayedSongFingerprint

class LastPlayedSongItemDecoration(dp: Float) : CoreItemDecoration(dp) {
    private val offset = 16
    private val itemViewType = LastPlayedSongFingerprint().getLayoutRes()

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)

        val childViewHolder = parent.getChildViewHolder(view)
        if (childViewHolder.itemViewType != itemViewType) return

        val margin = (offset * dp).toInt()
        outRect.set(margin, margin, margin, 0)
    }
}