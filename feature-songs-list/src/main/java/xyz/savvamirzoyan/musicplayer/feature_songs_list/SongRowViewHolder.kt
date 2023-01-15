package xyz.savvamirzoyan.musicplayer.feature_songs_list

import androidx.core.view.isVisible
import xyz.savvamirzoyan.musicplayer.appcore.CoreViewHolder
import xyz.savvamirzoyan.musicplayer.appcore.load
import xyz.savvamirzoyan.musicplayer.appcore.setText
import xyz.savvamirzoyan.musicplayer.feature_songs_list.databinding.LayoutSongRowBinding

class SongRowViewHolder(
    binding: LayoutSongRowBinding
) : CoreViewHolder<LayoutSongRowBinding, SongUi>(binding) {

    override fun bind(item: SongUi) {
        binding.ivAlbumPicture.load(item.albumPictureUrl)
        binding.tvSongTitle.setText(item.title)
        binding.tvArtistName.setText(item.artist)
        binding.ivIsExplicit.isVisible = item.isExplicit

        binding.root.setOnClickListener { item.onClickListener(item.id, item.albumId) }
    }
}