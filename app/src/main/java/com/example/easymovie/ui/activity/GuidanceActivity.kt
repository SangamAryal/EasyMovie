package com.example.easymovie.ui.activity

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.leanback.app.GuidedStepSupportFragment
import com.example.easymovie.R
import com.example.easymovie.ui.fragments.GuidanceFragment
import com.example.easymovie.ui.fragments.guidedfragments.RentOptionsFragment


class GuidanceActivity : FragmentActivity() {
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setBackgroundDrawable(
            ColorDrawable(
                ContextCompat.getColor(
                    this,
                    R.color.default_background
                )
            )
        )

        if (savedInstanceState == null) {
            val fragment: GuidedStepSupportFragment = RentOptionsFragment()
            GuidedStepSupportFragment.addAsRoot(this, fragment, android.R.id.content)
        }
    }
}