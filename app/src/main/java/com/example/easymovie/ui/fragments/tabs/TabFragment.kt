package com.example.easymovie.ui.fragments.tabs

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.easymovie.R
import com.example.easymovie.databinding.OverviewBinding
import com.example.easymovie.interfaces.OnFragmentKeyListener


class TabFragment : Fragment() {
    private var callback: OverviewFocusCallback? = null
    private lateinit var overviewBinding: OverviewBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        overviewBinding = OverviewBinding.inflate(inflater, container, false)
        callback?.onOverviewFocusView(overviewBinding.episodeFragment)
        return overviewBinding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OverviewFocusCallback) {
            callback = context
        } else {
            throw RuntimeException("$context must implement OverviewFocusCallback")
        }
    }

    interface OverviewFocusCallback {
        fun onOverviewFocusView(view: View)
    }

    fun setOverviewFocusCallback(callback: OverviewFocusCallback) {
        this.callback = callback
    }

    override fun onDetach() {
        super.onDetach()
        callback = null
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