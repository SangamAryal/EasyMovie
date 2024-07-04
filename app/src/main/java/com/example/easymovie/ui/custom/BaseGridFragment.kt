package com.example.easymovie.ui.custom

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.leanback.app.VerticalGridSupportFragment
import androidx.leanback.widget.BrowseFrameLayout


abstract class BaseGridFragment : Fragment() {

    private var lifecycleCallbacks: FragmentManager.FragmentLifecycleCallbacks? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleCallbacks = object : FragmentManager.FragmentLifecycleCallbacks() {
            override fun onFragmentStarted(fm: FragmentManager, f: Fragment) {
                if (f is VerticalGridSupportFragment) {
                    val browseFrameLayout = f.view?.findViewById<View>(
                        androidx.leanback.R.id.grid_frame
                    ) as? BrowseFrameLayout
                    browseFrameLayout?.onFocusSearchListener = null
                }
            }
        }
        childFragmentManager.registerFragmentLifecycleCallbacks(lifecycleCallbacks!!, true)
    }

    override fun onDestroyView() {
        childFragmentManager.unregisterFragmentLifecycleCallbacks(lifecycleCallbacks!!)
        super.onDestroyView()
    }
}
