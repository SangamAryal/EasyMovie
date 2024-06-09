// PageRowFragmentFactory.kt
package com.example.easymovie.ui.page

import androidx.fragment.app.Fragment
import androidx.leanback.app.BrowseSupportFragment.FragmentFactory
import androidx.leanback.widget.PageRow
import com.example.easymovie.ui.fragments.*

class PageRowFragmentFactory : FragmentFactory<Fragment>() {
    override fun createFragment(row: Any): Fragment {
        val headerItem = (row as PageRow).headerItem

        return when (headerItem.id.toInt()) {
            HEADER_ID_1 -> MoviesRowFragment()
            HEADER_ID_2 -> GridShowFragment()
            HEADER_ID_3 -> SettingsFragment()
            else -> throw IllegalArgumentException("Invalid row $row")
        }
    }

    companion object {
        const val HEADER_ID_1 = 1
        const val HEADER_ID_2 = 2
        const val HEADER_ID_3 = 3

    }
}


