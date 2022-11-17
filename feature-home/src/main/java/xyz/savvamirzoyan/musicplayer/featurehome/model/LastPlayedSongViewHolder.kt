package xyz.savvamirzoyan.musicplayer.featurehome.model

import androidx.core.view.isVisible
import xyz.savvamirzoyan.musicplayer.appcore.CoreViewHolder
import xyz.savvamirzoyan.musicplayer.appcore.load
import xyz.savvamirzoyan.musicplayer.featurehome.databinding.LayoutSongRowBinding

class LastPlayedSongViewHolder(
    binding: LayoutSongRowBinding
) : CoreViewHolder<LayoutSongRowBinding, LastPlayedSongUi>(binding) {

    override fun bind(item: LastPlayedSongUi) {
        binding.ivAlbumPicture.load(item.albumPictureUrl)
        binding.tvSongTitle.text = item.title.get(binding.root.context)
        binding.tvArtistName.text = item.artist.get(binding.root.context)
        binding.ivIsExplicit.isVisible = item.isExplicit
    }
}