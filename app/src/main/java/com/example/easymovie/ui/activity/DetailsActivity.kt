package com.example.easymovie.ui.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.KeyEvent
import androidx.fragment.app.FragmentActivity
import com.example.easymovie.R
import com.example.easymovie.interfaces.OnFragmentKeyListener
import com.example.easymovie.ui.fragments.MovieDetailsFragment
import com.example.easymovie.ui.fragments.MoviesRowFragment

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