package com.example.easymovie.ui.activity


import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.fragment.app.FragmentActivity
import com.example.easymovie.R
import com.example.easymovie.ui.fragments.MainFragment
import com.example.easymovie.ui.fragments.SearchFragment


class SearchActivity : LeanbackActivity() {
    private var mFragment: SearchFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("SearchActivity", "onCreate: ")
        enableEdgeToEdge()
        setContentView(R.layout.activity_search)
        mFragment =
            supportFragmentManager.findFragmentById(R.id.searchFragment) as SearchFragment?
    }

    override fun onSearchRequested(): Boolean {
        Log.d("SearchActivity", "onSearchReq: ")
        if (mFragment!!.hasResults()) {
            startActivity(Intent(this, SearchActivity::class.java))
        } else {
            mFragment?.startRecognition()
        }
        return true
    }


}