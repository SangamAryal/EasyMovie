package com.example.easymovie.ui.fragments.tabs

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.leanback.app.RowsSupportFragment
import androidx.leanback.widget.ArrayObjectAdapter
import androidx.leanback.widget.HeaderItem
import androidx.leanback.widget.ListRow
import androidx.leanback.widget.ListRowPresenter
import androidx.lifecycle.ViewModelProvider
import com.example.easymovie.application.MyApplication
import com.example.easymovie.data.api.Response
import com.example.easymovie.data.model.movielist.Result
import com.example.easymovie.ui.presenter.CardPresenter
import com.example.easymovie.viewmodels.MovieListViewModelFactory
import com.example.easymovie.viewmodels.MoviesListViewModel

class EpisodeFragment: RowsSupportFragment() {
    private val repository by lazy { (requireActivity().application as MyApplication).moviesRepository }
    private lateinit var mainViewModel: MoviesListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        val presenter = CardPresenter()
        val rowAdapter = ArrayObjectAdapter(presenter)

        result?.forEach { result ->
            rowAdapter.add(result)
        }

        val header = HeaderItem(0, "")
        val listRow = ListRow(header, rowAdapter)

        val rowPresenter = ListRowPresenter()
        val adapter = ArrayObjectAdapter(rowPresenter)
        adapter.add(listRow)

        setAdapter(adapter)
    }
    companion object{
        const val TAG = "EpisodeFragment"
    }
}