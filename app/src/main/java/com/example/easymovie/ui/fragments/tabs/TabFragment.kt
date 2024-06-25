package com.example.easymovie.ui.fragments.tabs

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.easymovie.R
import com.example.easymovie.interfaces.OnFragmentKeyListener


class TabFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.overview, container, false)
        Log.d(TAG, "onCreateView: ")
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val transaction = childFragmentManager.beginTransaction()
        transaction.replace(R.id.episode_fragment, EpisodeFragment()).commit()
    }



    companion object {
        private const val TAG = "TabFragment"

    }
}