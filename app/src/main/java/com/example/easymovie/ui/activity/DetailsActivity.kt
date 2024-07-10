package com.example.easymovie.ui.activity

import PageFragment
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction
import com.bumptech.glide.Glide
import com.example.easymovie.R
import com.example.easymovie.data.model.movielist.Result
import com.example.easymovie.databinding.DetailMainBinding
import com.example.easymovie.databinding.LeftDetailBinding
import com.example.easymovie.databinding.RightDetailBinding
import com.example.easymovie.databinding.TopBarBinding
import com.example.easymovie.ui.fragments.tabs.EpisodeFragment
import com.example.easymovie.ui.fragments.tabs.TabFragment
import com.example.easymovie.utils.Constants.IMAGE_BASE_URL

class DetailsActivity : FragmentActivity(){

    private var mSelectedMovie: Result? = null
    private var lastFocusedTab: View? = null
    private var lastSelectedTabIndex: Int = 0

    private lateinit var detailMainBinding: DetailMainBinding
    private lateinit var topBarBinding: TopBarBinding
    private lateinit var leftDetailBinding: LeftDetailBinding
    private lateinit var rightDetailBinding: RightDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        detailMainBinding = DetailMainBinding.inflate(layoutInflater)
        setContentView(detailMainBinding.root)

        topBarBinding = TopBarBinding.bind(detailMainBinding.root.findViewById(R.id.topp_bar))
        leftDetailBinding =
            LeftDetailBinding.bind(detailMainBinding.root.findViewById(R.id.left_part))
        rightDetailBinding =
            RightDetailBinding.bind(detailMainBinding.root.findViewById(R.id.right_part))

        mSelectedMovie = this.intent.getSerializableExtra(MOVIE) as Result

        // Set up tabs
        setupTabs()

        // Load initial fragment
        loadFragment(PageFragment.newInstance(1))

        mSelectedMovie?.poster_path?.let { posterPath ->
            val url = IMAGE_BASE_URL + posterPath
            Glide.with(this).load(url).into(leftDetailBinding.imageViewLeft)
        }

        leftDetailBinding.title.text = mSelectedMovie?.title
        leftDetailBinding.subtitle.text = mSelectedMovie?.original_title
        leftDetailBinding.description.text = mSelectedMovie?.overview

        rightDetailBinding.title.text = mSelectedMovie?.title

        // Set up play button
        detailMainBinding.playButton.setOnClickListener {
            val intent = Intent(this, PlaybackActivity::class.java)
            intent.putExtra(MOVIE, mSelectedMovie)
            startActivity(intent)
        }

        // Set focus change listeners
        setFocusListener(topBarBinding.browse)
        setFocusListener(topBarBinding.search)
        setFocusListener(topBarBinding.notificationIcon)
        setFocusListener(topBarBinding.profilePic)

        // Set key listeners
        setOnKeyListenerForView(
            topBarBinding.browse, leftTargetId = null, rightTargetId = R.id.search,
            upTargetId = null, downTargetId = R.id.play_button
        )
        setOnKeyListenerForView(
            topBarBinding.search, leftTargetId = R.id.browse, rightTargetId = R.id.notification_icon,
            upTargetId = null, downTargetId = R.id.play_button
        )
        setOnKeyListenerForView(
            topBarBinding.notificationIcon, leftTargetId = R.id.search, rightTargetId = R.id.profile_pic,
            upTargetId = null, downTargetId = R.id.play_button
        )
        setOnKeyListenerForView(
            topBarBinding.profilePic, leftTargetId = R.id.notification_icon, rightTargetId = null,
            upTargetId = null, downTargetId = R.id.play_button
        )
        setOnKeyListenerForView(
            rightDetailBinding.tab1, leftTargetId = R.id.play_button, rightTargetId = R.id.tab2,
            upTargetId = R.id.profile_pic, downTargetId = R.id.episode_fragment
        )
        try {
            setOnKeyListenerForView(
                rightDetailBinding.tab2, leftTargetId = R.id.tab1, rightTargetId = R.id.tab3,
                upTargetId = R.id.profile_pic, downTargetId =R.id.fragment_container
            )
        } catch (e: Exception) {
            Log.d(TAG, "tab2 is error is $e")
        }
        setOnKeyListenerForView(
            rightDetailBinding.tab3, leftTargetId = R.id.tab2, rightTargetId = R.id.tab4,
            upTargetId = R.id.profile_pic, downTargetId = R.id.episode_fragment
        )
        setOnKeyListenerForView(
            rightDetailBinding.tab4, leftTargetId = R.id.tab3, rightTargetId = null,
            upTargetId = R.id.profile_pic, downTargetId = R.id.episode_fragment
        )
        setOnKeyListenerForView(
            detailMainBinding.playButton, leftTargetId = null, rightTargetId = R.id.tab1,
            upTargetId = R.id.search, downTargetId = null
        )
        setOnKeyListenerForView(
            rightDetailBinding.fragmentContainer, leftTargetId = null, rightTargetId =null,
            upTargetId = R.id.tab1, downTargetId = null
        )

        detailMainBinding.playButton.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                lastSelectedTabIndex = 0
            }
        }


        topBarBinding.browse.requestFocus()
    }

    fun getLastFocusedView(): View? {
        return lastFocusedTab
    }



    private fun setupTabs() {
        val tabs = listOf(
            rightDetailBinding.tab1 to 1,
            rightDetailBinding.tab2 to 2,
            rightDetailBinding.tab3 to 3,
            rightDetailBinding.tab4 to 4
        )

        for ((tab, index) in tabs) {
            tab.setOnClickListener {
                loadFragment(PageFragment.newInstance(index), index > lastSelectedTabIndex)
                lastSelectedTabIndex = index
            }
            tab.setOnFocusChangeListener { v, hasFocus ->
                if (hasFocus) {
                    lastFocusedTab = v
                    v.elevation = 8.0f
                    if (v is TextView) {
                        v.setTypeface(null, Typeface.BOLD)
                        v.setTextColor(Color.WHITE)
                        loadFragment(PageFragment.newInstance(index), index > lastSelectedTabIndex)
                        lastSelectedTabIndex = index
                    }
                } else {
                    v.elevation = 0f
                    if (v is TextView) {
                        v.setTypeface(null, Typeface.NORMAL)
                        v.setTextColor(ContextCompat.getColor(this, R.color.bar))
                    }
                }
            }
        }
    }
    private fun loadFragment(fragment: Fragment, isForward: Boolean=true) {
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        if (isForward) {
            transaction.setCustomAnimations(
                R.anim.slide_in_right,  // enter animation
                R.anim.slide_out_left,  // exit animation
                R.anim.slide_in_left,   // pop enter animation
                R.anim.slide_out_right  // pop exit animation
            )
        } else {
            transaction.setCustomAnimations(
                R.anim.slide_in_left,
                R.anim.slide_out_right,
                R.anim.slide_in_right,
                R.anim.slide_out_left
            )
        }

        transaction.replace(R.id.fragment_container, fragment)
        transaction.commit()
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



    private fun changeDrawableColor(textView: TextView, colorResId: Int) {
        for (drawable in textView.compoundDrawables) {
            drawable?.setColorFilter(
                ContextCompat.getColor(this, colorResId), android.graphics.PorterDuff.Mode.SRC_IN
            )
        }
    }

    private fun setOnKeyListenerForView(
        view: View,
        rightTargetId: Int? = null,
        leftTargetId: Int? = null,
        upTargetId: Int? = null,
        downTargetId: Int? = null
    ) {
        view.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN) {
                when (keyCode) {
                    KeyEvent.KEYCODE_DPAD_RIGHT -> {
                        rightTargetId?.let { findViewById<View>(it).requestFocus() }
                        true
                    }
                    KeyEvent.KEYCODE_DPAD_LEFT -> {
                        leftTargetId?.let { findViewById<View>(it).requestFocus() }
                        true
                    }
                    KeyEvent.KEYCODE_DPAD_UP -> {
                        upTargetId?.let { findViewById<View>(it).requestFocus() }
                        true
                    }
                    KeyEvent.KEYCODE_DPAD_DOWN -> {
                        downTargetId?.let { findViewById<View>(it).requestFocus() }
                        true
                    }
                    else -> false
                }
            } else {
                false
            }
        }
    }




    companion object {
        const val TAG = "DetailsActivity"
        const val SHARED_ELEMENT_NAME = "hero"
        const val MOVIE = "Movie"
    }
}
