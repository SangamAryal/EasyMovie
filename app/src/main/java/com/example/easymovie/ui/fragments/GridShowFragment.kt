package com.example.easymovie.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.leanback.app.BrowseSupportFragment
import androidx.leanback.app.VerticalGridSupportFragment
import androidx.leanback.widget.ArrayObjectAdapter
import androidx.leanback.widget.VerticalGridPresenter
import com.example.easymovie.application.MyApplication
import com.example.easymovie.data.api.Response
import com.example.easymovie.data.model.movielist.Result
import com.example.easymovie.ui.presenter.GridItemPresenter
import com.example.easymovie.viewmodels.MovieListViewModelFactory
import com.example.easymovie.viewmodels.MoviesListViewModel

class GridShowFragment : VerticalGridSupportFragment(), BrowseSupportFragment.MainFragmentAdapterProvider {

    private val repository by lazy { (requireActivity().application as MyApplication).moviesRepository }
    private lateinit var mainViewModel: MoviesListViewModel
    private lateinit var mAdapter: ArrayObjectAdapter

    override fun getMainFragmentAdapter(): BrowseSupportFragment.MainFragmentAdapter<*> {
        return BrowseSupportFragment.MainFragmentAdapter(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val gridPresenter = VerticalGridPresenter().apply {
            numberOfColumns = 4
        }
        setGridPresenter(gridPresenter)

        // Initialize the adapter
        mAdapter = ArrayObjectAdapter(GridItemPresenter())
        adapter = mAdapter
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.setPadding(0, 50, 0, 0)

        mainViewModel = ViewModelProvider(
            this, MovieListViewModelFactory(repository)
        )[MoviesListViewModel::class.java]

        mainViewModel.moviesList.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Response.Success -> loadGridItems(result.data?.results ?: emptyList())
                is Response.Error -> {
                    loadGridItems(emptyList())
                    Log.e("GridFragment", "Error fetching movies: ${result.error}")
                }

                is Response.Loading -> TODO()
            }
        }
    }

    private fun loadGridItems(movieList: List<Result>) {
        if (movieList.isEmpty()) {
            Log.e("AccountFragment", "Movie list is empty, no items to load.")
            return
        }
        mAdapter.setItems(movieList, null)
        adapter =mAdapter
    }


}
