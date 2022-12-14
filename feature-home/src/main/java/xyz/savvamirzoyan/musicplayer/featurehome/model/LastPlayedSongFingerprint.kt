package xyz.savvamirzoyan.musicplayer.featurehome.model

import android.view.LayoutInflater
import android.view.ViewGroup
import xyz.savvamirzoyan.musicplayer.appcore.CoreViewHolder
import xyz.savvamirzoyan.musicplayer.appcore.CoreViewHolderFingerprint
import xyz.savvamirzoyan.musicplayer.core.Model
import xyz.savvamirzoyan.musicplayer.featurehome.R
import xyz.savvamirzoyan.musicplayer.featurehome.databinding.LayoutSongRowBinding

class LastPlayedSongFingerprint : CoreViewHolderFingerprint<LayoutSongRowBinding, LastPlayedSongUi> {

    override fun isRelativeItem(item: Model.Ui) = item is LastPlayedSongUi
    override fun getLayoutRes() = R.layout.layout_song_row
    override fun getViewHolder(
        layoutInflater: LayoutInflater,
        parent: ViewGroup
    ): CoreViewHolder<LayoutSongRowBinding, LastPlayedSongUi> {
        val binding = LayoutSongRowBinding.inflate(layoutInflater, parent, false)
        return LastPlayedSongViewHolder(binding)
    }
}