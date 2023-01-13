package xyz.savvamirzoyan.musicplayer.featurehome.model

import androidx.core.view.isVisible
import xyz.savvamirzoyan.musicplayer.appcore.CoreViewHolder
import xyz.savvamirzoyan.musicplayer.appcore.load
import xyz.savvamirzoyan.musicplayer.featurehome.databinding.LayoutSongRowBinding

class SongRowViewHolder(
    binding: LayoutSongRowBinding
) : CoreViewHolder<LayoutSongRowBinding, SongUi>(binding) {

    override fun bind(item: SongUi) {
        binding.ivAlbumPicture.load(item.albumPictureUrl)
        binding.tvSongTitle.text = item.title.get(binding.root.context)
        binding.tvArtistName.text = item.artist.get(binding.root.context)
        binding.ivIsExplicit.isVisible = item.isExplicit

        binding.root.setOnClickListener { item.onClickListener(item.id, item.albumId) }
    }
}