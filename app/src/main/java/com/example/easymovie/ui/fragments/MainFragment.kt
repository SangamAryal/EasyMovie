package com.example.easymovie.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.leanback.app.BrowseSupportFragment
import androidx.leanback.widget.ArrayObjectAdapter
import androidx.leanback.widget.HeaderItem
import androidx.leanback.widget.ImageCardView
import androidx.leanback.widget.ListRow
import androidx.leanback.widget.ListRowPresenter
import androidx.leanback.widget.OnItemViewClickedListener
import androidx.leanback.widget.Presenter
import androidx.leanback.widget.Row
import androidx.leanback.widget.RowPresenter
import androidx.lifecycle.ViewModelProvider
import com.example.easymovie.R
import com.example.easymovie.application.MyApplication
import com.example.easymovie.data.api.Response
import com.example.easymovie.data.model.MovieList.MovieList
import com.example.easymovie.data.model.MovieList.Result
import com.example.easymovie.ui.activity.DetailsActivity
import com.example.easymovie.ui.presenter.CardPresenter
import com.example.easymovie.viewmodels.MovieListViewModelFactory
import com.example.easymovie.viewmodels.MoviesListViewModel
import java.io.Serializable
import java.util.Collections


class MainFragment : BrowseSupportFragment() {
    private val repository by lazy { (requireActivity().application as MyApplication).moviesRepository }
    private lateinit var mainViewModel: MoviesListViewModel


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupUIElements()
        prepareEntranceTransition()
        mainViewModel = ViewModelProvider(
            this, MovieListViewModelFactory(repository)
        )[MoviesListViewModel::class.java]
        mainViewModel.moviesList.observe(viewLifecycleOwner) { result ->
            Log.d("MainFragment", "Received movies list: $result")
            if (result is Response.Success) {
                val movieList = result.data?.results
                Log.d("MainFragment", "Movie list size: ${movieList?.size}")
                if (movieList != null) {
                    loadRows(movieList)
                }
            } else if (result is Response.Error) {
                Log.e("MainFragment", "Error fetching movies: ${result.error}")
                loadRows(emptyList())
                // Handle error
            }
        }
        Log.d("MainFragment", "onActivityCreated")
        setupEventListeners()


    }

    private fun setupUIElements() {
        title = getString(R.string.browse_title)
        // over title
        headersState = HEADERS_ENABLED
        isHeadersTransitionOnBackEnabled = true

        // set fastLane (or headers) background color
        brandColor = ContextCompat.getColor(requireActivity(), R.color.fastlane_background)
        // set search icon color
        searchAffordanceColor = ContextCompat.getColor(requireActivity(), R.color.search_opaque)
    }

    private fun loadRows(movieList: List<Result>) {
        if (movieList.isEmpty()) {
            Log.e("MainFragment", "Movie list is empty, no rows to load.")
            return
        }

        val rowsAdapter = ArrayObjectAdapter(ListRowPresenter())
        val cardPresenter = CardPresenter()
        val categories = listOf("Upcoming", "Now Playing", "Action", "Comedy", "Drama", "Sci-Fi", "Horror")

        val numCols = movieList.size

        for (i in 0 until NUM_ROWS) {
            try {
                if (i != 0) {
                    Collections.shuffle(movieList)
                }
                val listRowAdapter = ArrayObjectAdapter(cardPresenter)
                for (j in 0 until numCols) {
                    listRowAdapter.add(movieList[j % movieList.size])
                }
                val header = HeaderItem(i.toLong(), categories[i % categories.size])
                rowsAdapter.add(ListRow(header, listRowAdapter))
            } catch (e: Exception) {
                Log.e("MainFragment", "Error loading row $i: ${e.message}")
            }
        }

        adapter = rowsAdapter
        startEntranceTransition()
    }

    private fun setupEventListeners() {
        setOnSearchClickedListener {
            Toast.makeText(requireActivity(), "Search is yet to be implemented", Toast.LENGTH_LONG)
                .show()
        }
        onItemViewClickedListener = ItemViewClickedListener()

    }

    private inner class ItemViewClickedListener : OnItemViewClickedListener {
        override fun onItemClicked(
            itemViewHolder: Presenter.ViewHolder,
            item: Any,
            rowViewHolder: RowPresenter.ViewHolder,
            row: Row
        ) {

            if (item is Result) {
                Log.d("ItemClicked", "Item: $item")
                val intent = Intent(activity!!, DetailsActivity::class.java)
                intent.putExtra(DetailsActivity.MOVIE, item as Serializable)

                val bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    activity!!,
                    (itemViewHolder.view as ImageCardView).mainImageView,
                    DetailsActivity.SHARED_ELEMENT_NAME
                )
                    .toBundle()
                startActivity(intent, bundle)
            } else if (item is String) {
//                if (item.contains(getString(R.string.error_fragment))) {
//                    val intent = Intent(activity!!, BrowseErrorActivity::class.java)
//                    startActivity(intent)
//                } else {
//                    Toast.makeText(activity!!, item, Toast.LENGTH_SHORT).show()
//                }
            }
        }
    }


    companion object {
        private val NUM_ROWS = 7
    }


}


