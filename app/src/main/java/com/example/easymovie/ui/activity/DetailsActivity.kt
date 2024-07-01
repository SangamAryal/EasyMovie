package com.example.easymovie.ui.activity

import PageFragment
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
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
import com.example.easymovie.ui.fragments.tabs.TabFragment
import com.example.easymovie.utils.Constants.IMAGE_BASE_URL

class DetailsActivity : FragmentActivity(), TabFragment.OverviewFocusCallback {

    private var mSelectedMovie: Result? = null

    private lateinit var detailMainBinding: DetailMainBinding
    private lateinit var topBarBinding: TopBarBinding
    private lateinit var leftDetailBinding: LeftDetailBinding
    private lateinit var rightDetailBinding: RightDetailBinding

    private var overviewRowView: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        detailMainBinding = DetailMainBinding.inflate(layoutInflater)
        setContentView(detailMainBinding.root)

        // Initialize bindings for included layouts
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

        topBarBinding.browse.requestFocus()
    }

    override fun onOverviewFocusView(view: View) {
        overviewRowView = view
        setFocusListener(view)
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
                loadFragment(PageFragment.newInstance(index))
            }
            tab.setOnFocusChangeListener { v, hasFocus ->
                if (hasFocus) {
                    v.elevation = 8.0f
                    if (v is TextView) {
                        v.setTypeface(null, Typeface.BOLD)
                        v.setTextColor(Color.WHITE)
                        loadFragment(PageFragment.newInstance(index))
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

    private fun loadFragment(fragment: Fragment) {
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        if (fragment is TabFragment) {
            fragment.setOverviewFocusCallback(this)
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

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        val focusedView = currentFocus
        return when (keyCode) {
            KeyEvent.KEYCODE_DPAD_LEFT -> {
                handleDpadLeft(focusedView)
                true
            }

            KeyEvent.KEYCODE_DPAD_RIGHT -> {
                handleDpadRight(focusedView)
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
            R.id.search -> topBarBinding.browse.requestFocus()
            R.id.notification_icon -> topBarBinding.search.requestFocus()
            R.id.profile_pic -> topBarBinding.notificationIcon.requestFocus()
            R.id.tab1 -> detailMainBinding.playButton.requestFocus()
            R.id.tab2 -> rightDetailBinding.tab1.requestFocus()
            R.id.tab3 -> rightDetailBinding.tab2.requestFocus()
            R.id.tab4 -> rightDetailBinding.tab3.requestFocus()
        }
    }

    private fun handleDpadRight(focusedView: View?) {
        when (focusedView?.id) {
            R.id.browse -> topBarBinding.search.requestFocus()
            R.id.search -> topBarBinding.notificationIcon.requestFocus()
            R.id.notification_icon -> topBarBinding.profilePic.requestFocus()
            R.id.play_button -> rightDetailBinding.tab1.requestFocus()
            R.id.tab1 -> rightDetailBinding.tab2.requestFocus()
            R.id.tab2 -> rightDetailBinding.tab3.requestFocus()
            R.id.tab3 -> rightDetailBinding.tab4.requestFocus()
        }
    }

    private fun handleDpadUp(focusedView: View?) {
        when (focusedView) {
            overviewRowView -> {
                detailMainBinding.playButton.requestFocus()
            }
            rightDetailBinding.tab1 -> topBarBinding.browse.requestFocus()
            rightDetailBinding.tab2 -> rightDetailBinding.tab1.requestFocus()
            rightDetailBinding.tab3 -> rightDetailBinding.tab2.requestFocus()
            rightDetailBinding.tab4 -> rightDetailBinding.tab3.requestFocus()
            else -> topBarBinding.profilePic.requestFocus()
        }
    }

    private fun handleDpadDown(focusedView: View?) {
        when (focusedView?.id) {
            R.id.tab1 -> overviewRowView?.requestFocus()

            R.id.browse, R.id.search, R.id.notification_icon, R.id.profile_pic -> detailMainBinding.playButton.requestFocus()
        }
    }

    companion object {
        const val TAG = "DetailsActivity"
        const val SHARED_ELEMENT_NAME = "hero"
        const val MOVIE = "Movie"
    }
}


