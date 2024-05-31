
package com.example.easymovie.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.core.app.ActivityOptionsCompat
import androidx.leanback.app.SearchSupportFragment
import androidx.leanback.widget.ArrayObjectAdapter
import androidx.leanback.widget.HeaderItem
import androidx.leanback.widget.ImageCardView
import androidx.leanback.widget.ListRow
import androidx.leanback.widget.ListRowPresenter
import androidx.leanback.widget.ObjectAdapter
import androidx.leanback.widget.OnItemViewClickedListener
import androidx.leanback.widget.Presenter
import androidx.leanback.widget.Row
import androidx.leanback.widget.RowPresenter
import androidx.lifecycle.ViewModelProvider
import com.example.easymovie.R
import com.example.easymovie.application.MyApplication
import com.example.easymovie.data.model.movielist.Result
import com.example.easymovie.ui.activity.DetailsActivity
import com.example.easymovie.ui.presenter.CardPresenter
import com.example.easymovie.viewmodels.MovieListViewModelFactory
import com.example.easymovie.viewmodels.MoviesListViewModel
import java.io.Serializable

class SearchFragment : SearchSupportFragment(), SearchSupportFragment.SearchResultProvider {

    private val mHandler: Handler = Handler()
    private var mRowsAdapter: ArrayObjectAdapter? = null
    private var mQuery: String? = null
    private var mResultsFound = false
    private lateinit var mainViewModel: MoviesListViewModel

    private val repository by lazy {
        (activity?.application as? MyApplication)?.moviesRepository
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreateFragment")
        mRowsAdapter = ArrayObjectAdapter(ListRowPresenter())
        setSearchResultProvider(this)
        setOnItemViewClickedListener(ItemViewClickListener())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated")

        repository?.let {
            mainViewModel = ViewModelProvider(this, MovieListViewModelFactory(it))[MoviesListViewModel::class.java]
            observeSearchResults()
        } ?: run {
            Log.e(TAG, "Repository is not available")
        }
    }

    private fun observeSearchResults() {
        mainViewModel.searchResults.observe(viewLifecycleOwner) { results ->
            updateResults(results)
        }
    }

    override fun onPause() {
        mHandler.removeCallbacksAndMessages(null)
        super.onPause()
    }

    override fun getResultsAdapter(): ObjectAdapter {
        return mRowsAdapter!!
    }

    override fun onQueryTextChange(newQuery: String?): Boolean {
        loadQuery(newQuery ?: "")
        return true
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        loadQuery(query ?: "")
        return true
    }

    private fun loadQuery(query: String) {
        if (query.isNotBlank() && query != "nil") {
            mQuery = query
            mResultsFound = true
            mainViewModel.searchMovies(query)
        } else {
            mResultsFound = false
        }
    }

    fun hasResults(): Boolean {
        return mRowsAdapter!!.size() > 0 && mResultsFound
    }

    private fun updateResults(movies: List<Result>) {
        val titleRes = if (movies.isNotEmpty()) {
            R.string.search_results
        } else {
            R.string.no_search_results
        }
        val header = HeaderItem(getString(titleRes, mQuery))
        mRowsAdapter?.clear()
        val adapter = ArrayObjectAdapter(CardPresenter()).apply {
            addAll(0, movies)
        }
        val row = ListRow(header, adapter)
        mRowsAdapter?.add(row)
    }

    private inner class ItemViewClickListener : OnItemViewClickedListener{
        override fun onItemClicked(
            itemViewHolder: Presenter.ViewHolder?,
            item: Any?,
            rowViewHolder: RowPresenter.ViewHolder?,
            row: Row?
        ) {
            if(item is Result){
                val intent = Intent(activity!!, DetailsActivity::class.java)
                intent.putExtra(DetailsActivity.MOVIE, item as Serializable)

                val bundle = (itemViewHolder?.view as ImageCardView).mainImageView?.let {
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        activity!!,
                        it,
                        DetailsActivity.SHARED_ELEMENT_NAME
                    ).toBundle()
                }
                startActivity(intent, bundle)

            }
        }

    }


    companion object {
        private const val TAG = "SearchFragment"
    }
}
