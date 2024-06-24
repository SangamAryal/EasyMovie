package com.example.easymovie.ui.activity

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.leanback.tab.LeanbackTabLayout
import androidx.leanback.tab.LeanbackViewPager
import com.example.easymovie.R
import com.example.easymovie.ui.fragments.adapter.TabFragmentAdapter


class DetailsActivity : FragmentActivity() {
    private lateinit var viewPager: LeanbackViewPager
    private lateinit var tabLayout: LeanbackTabLayout

    private lateinit var logo: ImageView
    private lateinit var search: TextView
    private lateinit var browse: TextView
    private lateinit var notificationIcon: ImageView
    private lateinit var profilePic: ImageView

    private lateinit var imageViewLeft: ImageView
    private lateinit var titleTextView: TextView
    private lateinit var subtitleTextView: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var descriptionTextView: TextView

    private lateinit var playButton: ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detail_main)
//        if (savedInstanceState == null) {
//            supportFragmentManager.beginTransaction()
//                .replace(R.id.details_fragment, MovieDetailsFragment())
//                .commitNow()
//        }
//    }


        viewPager = findViewById(R.id.view_pager)
        tabLayout = findViewById(R.id.tab_layout)

        // Set up ViewPager with a FragmentStateAdapter
        viewPager.adapter = TabFragmentAdapter(supportFragmentManager)
        tabLayout.setupWithViewPager(viewPager)

        logo = findViewById(R.id.logo)
        search = findViewById(R.id.search)
        browse = findViewById(R.id.browse)
        notificationIcon = findViewById(R.id.notification_icon)
        profilePic = findViewById(R.id.profile_pic)


        imageViewLeft = findViewById(R.id.image_view_left)
        titleTextView = findViewById(R.id.title)
        subtitleTextView = findViewById(R.id.subtitle)
        progressBar = findViewById(R.id.progress)
        descriptionTextView = findViewById(R.id.description)

        playButton = findViewById(R.id.play_button)

        // Set focus change listeners
        setFocusListener(browse)
        setFocusListener(search)
        setFocusListener(notificationIcon)
        setFocusListener(profilePic)

        setFocusListenerImage(imageViewLeft)
        setFocusListener(titleTextView)
        setFocusListener(subtitleTextView)
        setFocusListener(progressBar)
        setFocusListener(descriptionTextView)


        logo.requestFocus()
    }


    private fun setFocusListener(view: View) {
        view.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                v.elevation = 8f
                if (v is TextView) {
                    v.setTypeface(null, Typeface.BOLD)
                    v.setTextColor(Color.WHITE)
                    changeDrawableColor(v, R.color.white_color)
                } else if (v is ImageView) {
                    v.setColorFilter(ContextCompat.getColor(this, R.color.white_color))
                }
            } else {
                v.elevation = 0f
                if (v is TextView) {
                    v.setTypeface(null, Typeface.NORMAL)
                    v.setTextColor(ContextCompat.getColor(this, R.color.bar))
                    changeDrawableColor(v, R.color.bar)
                } else if (v is ImageView) {
                    v.setColorFilter(ContextCompat.getColor(this, R.color.bar))
                }
            }
        }
    }

    private fun setFocusListenerImage(view: View) {
        view.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                v.alpha = 1f
            } else {
                v.alpha = 0.5f
            }
        }
    }

    private fun changeDrawableColor(textView: TextView, colorResId: Int) {
        for (drawable in textView.compoundDrawables) {
            drawable?.setColorFilter(
                ContextCompat.getColor(this, colorResId), android.graphics.PorterDuff.Mode.SRC_IN
            )
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        val focusedView = currentFocus
        return when (keyCode) {
            KeyEvent.KEYCODE_DPAD_LEFT -> {
                if (focusedView is LeanbackTabLayout) {
                    // Switch to the previous tab
                    val currentItem = viewPager.currentItem
                    Log.d(TAG, "onKeyDown: ")
                    if (currentItem > 0) {
                        viewPager.currentItem = currentItem - 1
                    }
                } else {
                    handleDpadLeft(focusedView)
                }
                true
            }

            KeyEvent.KEYCODE_DPAD_RIGHT -> {
                if (focusedView is LeanbackTabLayout) {
                    val currentItem = viewPager.currentItem
                    if (currentItem < (viewPager.id)) {
                        viewPager.currentItem = currentItem + 1
                    }
                } else {
                    handleDpadRight(focusedView)
                }
                true
            }

            KeyEvent.KEYCODE_DPAD_UP -> {
                handleDpadUp(focusedView)
                true
            }

            KeyEvent.KEYCODE_DPAD_DOWN -> {
                handleDpadDown(focusedView)
                true
            }

            else -> super.onKeyDown(keyCode, event)
        }
    }

    private fun handleDpadLeft(focusedView: View?) {
        when (focusedView?.id) {
            R.id.search -> browse.requestFocus()
            R.id.notification_icon -> search.requestFocus()
            R.id.profile_pic -> notificationIcon.requestFocus()
            R.id.browse -> logo.requestFocus()
        }
    }

    private fun handleDpadRight(focusedView: View?) {
        when (focusedView?.id) {
            R.id.logo -> browse.requestFocus()
            R.id.browse -> search.requestFocus()
            R.id.search -> notificationIcon.requestFocus()
            R.id.notification_icon -> profilePic.requestFocus()
            R.id.play_button -> tabLayout.requestFocus()
        }
    }

    private fun handleDpadUp(focusedView: View?) {
        when (focusedView?.id) {
            R.id.play_button -> logo.requestFocus()
            R.id.tab_layout -> logo.requestFocus()

        }
    }

    private fun handleDpadDown(focusedView: View?) {
        when (focusedView?.id) {
            R.id.logo -> playButton.requestFocus()
            R.id.browse -> playButton.requestFocus()
            R.id.search -> playButton.requestFocus()
            R.id.notification_icon -> playButton.requestFocus()
            R.id.profile_pic -> playButton.requestFocus()

        }
    }


    companion object {
        const val TAG = "DetailsActivity"
        const val SHARED_ELEMENT_NAME = "hero"
        const val MOVIE = "Movie"
    }
}