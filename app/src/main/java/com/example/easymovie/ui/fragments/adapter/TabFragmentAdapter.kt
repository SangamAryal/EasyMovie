package com.example.easymovie.ui.fragments.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.easymovie.ui.fragments.tabs.EpisodeFragment
import com.example.easymovie.ui.fragments.tabs.TabFragment

class TabFragmentAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getCount(): Int {
        return 4 // Number of tabs
    }

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> TabFragment()
            1 -> EpisodeFragment()
            2 -> TabFragment()
            3 -> TabFragment()
            else -> throw IllegalStateException("Unexpected position $position")
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 -> "OVERVIEW"
            1 -> "EPISODES"
            2 -> "TRAILERS & MORE"
            3 -> "MORE LIKE THIS"
            else -> null
        }
    }
}
