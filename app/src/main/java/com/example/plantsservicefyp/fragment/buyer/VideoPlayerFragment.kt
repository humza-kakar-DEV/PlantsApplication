package com.example.plantsservicefyp.fragment.buyer

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.graphics.Outline
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.example.plantsservicefyp.databinding.FragmentVideoPlayerBinding
import com.example.plantsservicefyp.util.constant.ChangeFragment
import com.example.plantsservicefyp.viewmodel.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class VideoPlayerFragment : Fragment() {

    private var player: ExoPlayer? = null

    private lateinit var binding: FragmentVideoPlayerBinding

    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentVideoPlayerBinding.inflate(layoutInflater, container, false)

        AnimatorSet().apply {
            playTogether(
                ObjectAnimator.ofFloat(binding.contentContainer, "translationY", 0f),
                ObjectAnimator.ofFloat(binding.contentContainer, "alpha", 1f),
            )
            duration = 1000L
            start()
        }

        binding.topAppBar.setNavigationOnClickListener {
            requireActivity()
                .supportFragmentManager
                .popBackStack()
        }

        binding.playerView.setOutlineProvider(object : ViewOutlineProvider() {
            override fun getOutline(view: View, outline: Outline) {
                outline.setRoundRect(0, 0, view.width, view.height, 15f)
            }
        })
        binding.playerView.setClipToOutline(true)

        sharedViewModel._observeVideoDetail.observe(viewLifecycleOwner) {videoDetail->
            player = ExoPlayer.Builder(requireContext()).build().apply {
                addMediaItem(
                    MediaItem.fromUri(videoDetail.videoLink)
                )
                prepare()
                playWhenReady = true
            }
            binding.playerView.player = player
            binding.titleTextView.text = videoDetail.title
            binding.descriptionTextView.text = videoDetail.description
        }

        return binding.root
    }

    override fun onStop() {
        super.onStop()
        releasePlayer()
    }

    private fun releasePlayer() {
        player?.stop()
    }
}