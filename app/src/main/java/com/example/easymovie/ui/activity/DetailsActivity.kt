package com.example.easymovie.ui.activity

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.example.easymovie.R
import com.example.easymovie.ui.fragments.MovieDetailsFragment

class DetailsActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.details_fragment, MovieDetailsFragment())
                .commitNow()
        }
    }

    companion object {
        const val SHARED_ELEMENT_NAME = "hero"
        const val MOVIE = "Movie"
    }
}