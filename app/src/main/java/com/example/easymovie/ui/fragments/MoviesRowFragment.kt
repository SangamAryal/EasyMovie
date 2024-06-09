package com.example.easymovie.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.core.app.ActivityOptionsCompat
import androidx.leanback.app.BrowseSupportFragment
import androidx.leanback.app.BrowseSupportFragment.MainFragmentAdapterProvider
import androidx.leanback.app.RowsSupportFragment
import androidx.leanback.widget.ArrayObjectAdapter
import androidx.leanback.widget.HeaderItem
import androidx.leanback.widget.ListRow
import androidx.leanback.widget.ListRowPresenter
import androidx.leanback.widget.OnItemViewClickedListener
import androidx.leanback.widget.Presenter
import androidx.leanback.widget.Row
import androidx.leanback.widget.RowPresenter
import androidx.lifecycle.ViewModelProvider
import com.example.easymovie.application.MyApplication
import com.example.easymovie.data.api.Response
import com.example.easymovie.data.model.movielist.Result
import com.example.easymovie.ui.activity.DetailsActivity
import com.example.easymovie.ui.presenter.CardPresenter
import com.example.easymovie.viewmodels.MovieListViewModelFactory
import com.example.easymovie.viewmodels.MoviesListViewModel
import java.io.Serializable
import java.util.Collections

class MoviesRowFragment : RowsSupportFragment(), MainFragmentAdapterProvider {
    private val repository by lazy { (requireActivity().application as MyApplication).moviesRepository }
    private lateinit var mainViewModel: MoviesListViewModel
    private lateinit var rowsAdapter: ArrayObjectAdapter

    override fun getMainFragmentAdapter(): BrowseSupportFragment.MainFragmentAdapter<*> {
        return BrowseSupportFragment.MainFragmentAdapter(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        rowsAdapter = ArrayObjectAdapter(ListRowPresenter())
        adapter = rowsAdapter

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.setPadding(0, 150, 0, 0)
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
        setupEventListeners()


    }

    private fun handleDpadDown() {
        view?.focusSearch(View.FOCUS_DOWN)?.requestFocus()
    }

    private fun loadRows(movieList: List<Result>?) {
        if (movieList.isNullOrEmpty()) {
            Log.e("AccountFragment", "Movie list is empty, no rows to load.")
            return
        }

        val rowsAdapter = ArrayObjectAdapter(ListRowPresenter())
        val cardPresenter = CardPresenter()
        val categories =
            listOf("Upcoming", "Now Playing", "Action", "Comedy", "Drama", "Sci-Fi", "Horror")

        for (i in 0 until NUM_ROWS) {
            try {
                if (i != 0) {
                    Collections.shuffle(movieList)
                }
                val listRowAdapter = ArrayObjectAdapter(cardPresenter)
                for (j in movieList.indices) {
                    listRowAdapter.add(movieList[j % movieList.size])
                }
                val header = HeaderItem(i.toLong(), categories[i % categories.size])
                rowsAdapter.add(ListRow(header, listRowAdapter))
            } catch (e: Exception) {
                Log.e("AccountFragment", "Error loading row $i: ${e.message}")
            }
        }

        adapter = rowsAdapter
    }

    private fun setupEventListeners() {
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
                val bundle = (itemViewHolder.view as? ImageView)?.let {
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        activity!!, it, DetailsActivity.SHARED_ELEMENT_NAME
                    ).toBundle()
                }
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
        private const val NUM_ROWS = 7
    }
}