
package com.example.easymovie.ui.fragments

import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.leanback.app.BrowseSupportFragment
import androidx.leanback.widget.ArrayObjectAdapter
import androidx.leanback.widget.HeaderItem
import androidx.leanback.widget.ListRowPresenter
import androidx.leanback.widget.PageRow
import com.example.easymovie.R
import com.example.easymovie.ui.page.PageRowFragmentFactory

class MainFragment : BrowseSupportFragment() {
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mainFragmentRegistry.registerFragment(
            PageRow::class.java, PageRowFragmentFactory()
        )
        setupUIElements()
        prepareEntranceTransition()
        setupPageRows()
        setupEventListeners()
    }

    private fun setupUIElements() {
        title = getString(R.string.browse_title)
        headersState = HEADERS_ENABLED
        isHeadersTransitionOnBackEnabled = true
        brandColor = ContextCompat.getColor(requireActivity(), R.color.fastlane_background)
        searchAffordanceColor = ContextCompat.getColor(requireActivity(), R.color.search_opaque)
    }

    private fun setupPageRows() {
        val rowsAdapter = ArrayObjectAdapter(ListRowPresenter())

        val headerItem1 = HeaderItem(PageRowFragmentFactory.HEADER_ID_1.toLong(), "Movies Row")
        val pageRow1 = PageRow(headerItem1)
        rowsAdapter.add(pageRow1)

        val headerItem2 = HeaderItem(PageRowFragmentFactory.HEADER_ID_2.toLong(), "Movies Grid")
        val pageRow2 = PageRow(headerItem2)
        rowsAdapter.add(pageRow2)

        val headerItem3 = HeaderItem(PageRowFragmentFactory.HEADER_ID_3.toLong(), "Settings")
        val pageRow3 = PageRow(headerItem3)
        rowsAdapter.add(pageRow3)

        adapter = rowsAdapter

        // Set the fragment factory with the fetched movie list

    }

    private fun setupEventListeners() {
        setOnSearchClickedListener {
            Toast.makeText(requireActivity(), "Search is yet to be implemented", Toast.LENGTH_LONG)
                .show()
        }
    }

}
