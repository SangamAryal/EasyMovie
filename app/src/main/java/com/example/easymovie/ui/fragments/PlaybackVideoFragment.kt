package com.example.easymovie.ui.fragments

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.leanback.app.VideoSupportFragment
import androidx.leanback.app.VideoSupportFragmentGlueHost
import androidx.leanback.media.PlaybackTransportControlGlue
import com.example.easymovie.data.model.MovieList.Result
import com.example.easymovie.ui.activity.DetailsActivity
import com.example.easymovie.ui.customplayback.Media3PlayerAdapter

class PlaybackVideoFragment : VideoSupportFragment() {

    private lateinit var mTransportControlGlue: PlaybackTransportControlGlue<Media3PlayerAdapter>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val (_, _, _, _, _, originalTitle, _, _, _, _, _, _, _, _) = activity?.intent?.getSerializableExtra(
            DetailsActivity.MOVIE
        ) as Result
        val videoUrl =
            "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/WhatCarCanYouGetForAGrand.mp4"
        val glueHost = VideoSupportFragmentGlueHost(this@PlaybackVideoFragment)
//        val playerAdapter = MediaPlayerAdapter(activity)

        val playerAdapter = Media3PlayerAdapter(requireActivity())
//        playerAdapter.setRepeatAction(PlaybackControlsRow.RepeatAction.INDEX_NONE)

        mTransportControlGlue = PlaybackTransportControlGlue(activity, playerAdapter)
        mTransportControlGlue.host = glueHost
        mTransportControlGlue.title = originalTitle
        mTransportControlGlue.subtitle = "This is subtitle"
        mTransportControlGlue.playerAdapter.setDataSource(Uri.parse(videoUrl))


        Log.d("PlaybackVideoFragment", "Initializing player")
        mTransportControlGlue.playWhenPrepared()
        mTransportControlGlue.isSeekEnabled = true
    }
}

