package com.example.easymovie.ui.fragments

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.leanback.app.VideoSupportFragment
import androidx.leanback.app.VideoSupportFragmentGlueHost
import androidx.leanback.widget.ArrayObjectAdapter
import androidx.leanback.widget.ClassPresenterSelector
import androidx.leanback.widget.HeaderItem
import androidx.leanback.widget.ListRow
import androidx.leanback.widget.ListRowPresenter
import androidx.lifecycle.ViewModelProvider
import com.example.easymovie.application.MyApplication
import com.example.easymovie.data.api.Response
import com.example.easymovie.data.model.movielist.Result
import com.example.easymovie.ui.activity.DetailsActivity
import com.example.easymovie.ui.custom.CustomPlaybackSeekDataProvider
import com.example.easymovie.ui.custom.VideoMediaPlayerGlue
import com.example.easymovie.ui.custom.customplayback.Media3PlayerAdapter
import com.example.easymovie.ui.presenter.CardPresenter
import com.example.easymovie.viewmodels.MovieListViewModelFactory
import com.example.easymovie.viewmodels.MoviesListViewModel

class PlaybackVideoFragment : VideoSupportFragment() {
    private lateinit var mTransportControlGlue: VideoMediaPlayerGlue<Media3PlayerAdapter>
    private val repository by lazy { (requireActivity().application as MyApplication).moviesRepository }
    private lateinit var mainViewModel: MoviesListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//
        val result = activity?.intent?.getSerializableExtra(
            DetailsActivity.MOVIE
        ) as Result
        val videoUrl =
            "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/WhatCarCanYouGetForAGrand.mp4"
        val glueHost = VideoSupportFragmentGlueHost(this@PlaybackVideoFragment)

        val playerAdapter = Media3PlayerAdapter(requireActivity())
        val seekSpeeds = intArrayOf(2, 4, 8)
        mTransportControlGlue = VideoMediaPlayerGlue(requireActivity(), playerAdapter, result)
        mTransportControlGlue.host = glueHost
        mTransportControlGlue.subtitle = "This is subtitle"
        mTransportControlGlue.playerAdapter.setDataSource(Uri.parse(videoUrl))
        val intervalInSeconds = 10 // Generate a thumbnail every 10 seconds

        val seekDataProvider =
            CustomPlaybackSeekDataProvider(videoUrl, requireContext(), intervalInSeconds)

        // Set the seek provider to the transport control glue
        mTransportControlGlue.seekProvider = seekDataProvider



        Log.d("PlaybackVideoFragment", "Initializing player")
        mTransportControlGlue.playWhenPrepared()
        mTransportControlGlue.isSeekEnabled = true
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainViewModel = ViewModelProvider(
            this, MovieListViewModelFactory(repository)
        )[MoviesListViewModel::class.java]
        val existingData = mainViewModel.moviesList.value
        if (existingData is Response.Success) {
            val movieList = existingData.data?.results ?: emptyList()
            setupRelatedVideosRow(movieList)
        } else if (existingData is Response.Error) {
            setupRelatedVideosRow(emptyList())
            Log.e(TAG, "Error fetching movies: ${existingData.error}")
        }

    }

    private fun setupRelatedVideosRow(result: List<Result>?) {
        Log.d(TAG, "setupRelatedVideosRow: Function called")
        (adapter.presenterSelector as ClassPresenterSelector).addClassPresenter(
            ListRow::class.java, ListRowPresenter()
        )
        val relatedMoviesAdapter = ArrayObjectAdapter(CardPresenter())
        result?.forEach { content ->
            relatedMoviesAdapter.add(content)
        }
        val row = ListRow(HeaderItem(0, "Related Movies"), relatedMoviesAdapter)
        (adapter as ArrayObjectAdapter).add(row)
    }

    companion object {
        private const val TAG = "PlaybackVideoFragment"
    }
}
