package com.example.easymovie.ui.fragments

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.leanback.app.BrowseSupportFragment
import androidx.leanback.app.HeadersSupportFragment
import androidx.leanback.widget.ArrayObjectAdapter
import androidx.leanback.widget.ClassPresenterSelector
import androidx.leanback.widget.ListRowPresenter
import androidx.leanback.widget.PageRow
import androidx.leanback.widget.Row
import com.example.easymovie.R
import com.example.easymovie.ui.activity.SearchActivity
import com.example.easymovie.ui.custom.menu.CustomRowPresenter
import com.example.easymovie.ui.custom.menu.MenuItem
import com.example.easymovie.ui.page.PageRowFragmentFactory


class MainFragment : BrowseSupportFragment() {

//    override fun onActivityCreated(savedInstanceState: Bundle?) {
//        super.onActivityCreated(savedInstanceState)
//
//    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainFragmentRegistry.registerFragment(
            PageRow::class.java, PageRowFragmentFactory()
        )
        checkStoragePermission()
        setupUIElements()
        prepareEntranceTransition()
        setupPageRows()
        setupEventListeners()
    }

    override fun onCreateHeadersSupportFragment(): HeadersSupportFragment? {
        val headersFragment = super.onCreateHeadersSupportFragment()
        if (headersFragment != null) {
            val presenterSelector = ClassPresenterSelector()
            presenterSelector.addClassPresenter(Row::class.java, CustomRowPresenter())
            headersFragment.presenterSelector = presenterSelector
        }
        return headersFragment
    }


    private fun checkStoragePermission() {
        if (ContextCompat.checkSelfPermission(
                requireActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                requireActivity(), Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            // Requesting the permission
            ActivityCompat.requestPermissions(
                requireActivity(), arrayOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ), STORAGE_PERMISSION_CODE
            )
        } else {
            Log.d(TAG, "checkStoragePermission: Permission is already granted")
        }

    }

    private fun setupUIElements() {
        title = getString(R.string.browse_title)
        headersState = HEADERS_ENABLED
        isHeadersTransitionOnBackEnabled = true
        brandColor = ContextCompat.getColor(requireActivity(), R.color.fastlane_background)
        searchAffordanceColor = ContextCompat.getColor(requireActivity(), R.color.search_opaque)

    }


    private fun setupPageRows() {
        try {
            val rowsAdapter = ArrayObjectAdapter(ListRowPresenter())

            // Create PageRow instances with custom MenuItem headers
            val headerItem1 = MenuItem(PageRowFragmentFactory.HEADER_ID_1.toLong(), "Movies Row", R.drawable.ic_movie)
            val pageRow1 = PageRow(headerItem1)
            rowsAdapter.add(pageRow1)

            val headerItem2 = MenuItem(PageRowFragmentFactory.HEADER_ID_2.toLong(), "Movies Grid", R.drawable.ic_tv)
            val pageRow2 = PageRow(headerItem2)
            rowsAdapter.add(pageRow2)

            val headerItem3 = MenuItem(PageRowFragmentFactory.HEADER_ID_3.toLong(), "Settings", R.drawable.ic_settings_settings)
            val pageRow3 = PageRow(headerItem3)
            rowsAdapter.add(pageRow3)

            adapter = rowsAdapter
        } catch (e: Exception) {
            Log.e(TAG, "error is:: $e" )
        }
    }

    private fun setupEventListeners() {
        setOnSearchClickedListener {
            val intent = Intent(activity, SearchActivity::class.java)
            startActivity(intent)
        }
    }


    companion object {
        private const val STORAGE_PERMISSION_CODE = 100
        private const val TAG = "MainFragment"
    }
}
