package com.example.easymovie.ui.activity

import android.os.Bundle

import androidx.fragment.app.FragmentActivity
import androidx.leanback.tab.LeanbackTabLayout
import androidx.leanback.tab.LeanbackViewPager
import com.example.easymovie.R
import com.example.easymovie.ui.fragments.adapter.TabFragmentAdapter


class DetailsActivity : FragmentActivity() {
    private lateinit var viewPager: LeanbackViewPager
    private lateinit var tabLayout: LeanbackTabLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detail_main)

        viewPager = findViewById(R.id.view_pager)
        tabLayout = findViewById(R.id.tab_layout)

        // Set up ViewPager with a FragmentStateAdapter
        viewPager.adapter = TabFragmentAdapter(supportFragmentManager)
        tabLayout.setupWithViewPager(viewPager)


//        if (savedInstanceState == null) {
//            supportFragmentManager.beginTransaction()
//                .replace(R.id.detail_fragment, DetailsMovieFragment())
//                .commitNow()
//        }
    }

//    @SuppressLint("RestrictedApi")
//    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
//        val fragment = supportFragmentManager.findFragmentById(R.id.details_fragment)
//        if (fragment is OnFragmentKeyListener) {
//            if (fragment.onKeyEvent(event.keyCode, event)) {
//                return true // The event was handled by the fragment
//            }
//        }
//        return super.dispatchKeyEvent(event)
//    }


    companion object {
        const val SHARED_ELEMENT_NAME = "hero"
        const val MOVIE = "Movie"
    }
}