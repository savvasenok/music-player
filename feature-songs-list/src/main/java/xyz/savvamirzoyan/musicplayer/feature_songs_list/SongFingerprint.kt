package xyz.savvamirzoyan.musicplayer.feature_songs_list

import android.view.LayoutInflater
import android.view.ViewGroup
import xyz.savvamirzoyan.musicplayer.appcore.CoreViewHolder
import xyz.savvamirzoyan.musicplayer.appcore.CoreViewHolderFingerprint
import xyz.savvamirzoyan.musicplayer.core.Model
import xyz.savvamirzoyan.musicplayer.feature_songs_list.databinding.LayoutSongRowBinding

class SongFingerprint : CoreViewHolderFingerprint<LayoutSongRowBinding, SongUi> {

    override fun isRelativeItem(item: Model.Ui) = item is SongUi
    override fun getLayoutRes() = R.layout.layout_song_row
    override fun getViewHolder(
        layoutInflater: LayoutInflater,
        parent: ViewGroup
    ): CoreViewHolder<LayoutSongRowBinding, SongUi> {
        val binding = LayoutSongRowBinding.inflate(layoutInflater, parent, false)
        return SongRowViewHolder(binding)
    }
}