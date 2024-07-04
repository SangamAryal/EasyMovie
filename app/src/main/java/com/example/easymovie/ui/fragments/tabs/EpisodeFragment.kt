package com.example.easymovie.ui.fragments.tabs

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.leanback.app.VerticalGridSupportFragment
import androidx.leanback.widget.ArrayObjectAdapter
import androidx.leanback.widget.VerticalGridPresenter
import androidx.lifecycle.ViewModelProvider
import com.example.easymovie.R
import com.example.easymovie.application.MyApplication
import com.example.easymovie.data.api.Response
import com.example.easymovie.data.model.movielist.Result
import com.example.easymovie.databinding.EpisodeBinding
import com.example.easymovie.databinding.OverviewBinding
import com.example.easymovie.ui.custom.BaseGridFragment
import com.example.easymovie.ui.presenter.EpCardPresenter
import com.example.easymovie.viewmodels.MovieListViewModelFactory
import com.example.easymovie.viewmodels.MoviesListViewModel

class EpisodeFragment : BaseGridFragment() {

    private val repository by lazy { (requireActivity().application as MyApplication).moviesRepository }
    private lateinit var mainViewModel: MoviesListViewModel
    private lateinit var mAdapter: ArrayObjectAdapter
    private lateinit var verticalGridFragment: VerticalGridSupportFragment
    private lateinit var episodeBinding: EpisodeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAdapter = ArrayObjectAdapter(EpCardPresenter())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        episodeBinding = EpisodeBinding.inflate(inflater, container, false)
        return episodeBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        verticalGridFragment =
            childFragmentManager.findFragmentById(R.id.vertical_grid_fragment) as VerticalGridSupportFragment

        val gridPresenter = VerticalGridPresenter().apply {
            numberOfColumns = 20
        }
        verticalGridFragment.setGridPresenter(gridPresenter)
        verticalGridFragment.adapter = mAdapter

        mainViewModel = ViewModelProvider(
            this, MovieListViewModelFactory(repository)
        )[MoviesListViewModel::class.java]

        mainViewModel.moviesList.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Response.Success -> loadGridItems(result.data?.results ?: emptyList())
                is Response.Error -> {
                    loadGridItems(emptyList())
                    Log.e(TAG, "Error fetching movies: ${result.error}")
                }

                is Response.Loading -> {
                    // Handle loading state if needed
                }
            }
        }
    }

    private fun loadGridItems(movieList: List<Result>) {
        if (movieList.isEmpty()) {
            Log.e(TAG, "Movie list is empty, no items to load.")
            return
        }
        mAdapter.setItems(movieList, null)
    }

    companion object {
        const val TAG = "EpisodeFragment"
    }
}