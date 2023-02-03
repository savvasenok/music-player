package xyz.savvamirzoyan.musicplayer.feature_songs_list

import androidx.core.view.isVisible
import xyz.savvamirzoyan.musicplayer.appcore.*
import xyz.savvamirzoyan.musicplayer.core.Model
import xyz.savvamirzoyan.musicplayer.feature_songs_list.databinding.LayoutSongRowBinding

class SongRowViewHolder(
    binding: LayoutSongRowBinding
) : CoreViewHolder<LayoutSongRowBinding, SongUi>(binding) {

    override fun bind(item: SongUi) {
        binding.playableImageView.image.load(item.albumPictureUrl)
        binding.tvSongTitle.setText(item.title)
        binding.tvArtistName.setText(item.artist)
        binding.ivIsExplicit.isVisible = item.isExplicit

        binding.root.setOnClickListener { item.onClickListener(item.id) }

        if (item.isPlaying) setPlayingStatus(true)
    }

    override fun <R : Model.Ui> bindPayload(item: SongUi, payload: List<R>) {
        payload.forEach {
            when (it) {
                is SongPlayingStatusPayload -> setPlayingStatus(it.isPlaying)
            }
        }
    }

    private fun setPlayingStatus(isPlaying: Boolean) {
        binding.playableImageView.setIsPlaying(isPlaying)
    }
}