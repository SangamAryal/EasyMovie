package com.example.easymovie.ui.fragments

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
import com.example.easymovie.data.model.MovieList.Result
import com.example.easymovie.ui.presenter.CardPresenter
import com.example.easymovie.viewmodels.MovieListViewModelFactory
import com.example.easymovie.viewmodels.MoviesListViewModel

class SettingsFragment : RowsSupportFragment() {
    private val repository by lazy { (requireActivity().application as MyApplication).moviesRepository }
    private lateinit var mainViewModel: MoviesListViewModel
    private lateinit var rowsAdapter: ArrayObjectAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        rowsAdapter = ArrayObjectAdapter(ListRowPresenter())
        adapter = rowsAdapter

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainViewModel = ViewModelProvider(
            this, MovieListViewModelFactory(repository)
        )[MoviesListViewModel::class.java]

        mainViewModel.moviesList.observe(viewLifecycleOwner) { result ->
            if (result is Response.Success) {
                val movieList = result.data?.results ?: emptyList()
                loadRows(movieList)
            } else if (result is Response.Error) {
                loadRows(emptyList())
                Log.e("MainFragment", "Error fetching movies: ${result.error}")
            }
        }
    }


    private fun loadRows(movieList: List<Result>?) {
        if (movieList == null) {
            Log.e("MoviesFragment", "Movie list is empty, no rows to load.")
            return
        }

        val cardPresenter = CardPresenter()
        val listRowAdapter = ArrayObjectAdapter(cardPresenter)
        for (movie in movieList) {
            listRowAdapter.add(movie)
        }

        val header = HeaderItem(0, "Movies")
        rowsAdapter.add(ListRow(header, listRowAdapter))
    }

    companion object {
    }

}