package com.example.easymovie.ui.custom

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.leanback.widget.SearchOrbView
import androidx.leanback.widget.TitleViewAdapter
import com.example.easymovie.R

class CustomTitleView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), TitleViewAdapter.Provider {

    private var mBadgeView: ImageView
    private var mTextView: TextView
    private var mSearchOrbView: SearchOrbView
    private var flags = TitleViewAdapter.FULL_VIEW_VISIBLE
    private var mHasSearchListener = false

    init {
        LayoutInflater.from(context).inflate(R.layout.custom_title, this, true)
        mBadgeView = findViewById(R.id.title_badge)
        mTextView = findViewById(R.id.title_text)
        mSearchOrbView = findViewById(R.id.title_orb)

        clipToPadding = false
        clipChildren = false
    }

    private var mTitleViewAdapter = object : TitleViewAdapter() {
        override fun getSearchAffordanceView(): View {
            return this@CustomTitleView.mSearchOrbView
        }

        override fun setOnSearchClickedListener(listener: OnClickListener?) {
            this@CustomTitleView.setOnSearchClickedListener(listener)
        }

        override fun setAnimationEnabled(enable: Boolean) {
            this@CustomTitleView.enableAnimation(enable)
        }

        override fun getBadgeDrawable(): Drawable? {
            return null
        }

        override fun getSearchAffordanceColors(): SearchOrbView.Colors? {
            return this@CustomTitleView.searchAffordanceColors
        }

        override fun getTitle(): CharSequence? {
            return this@CustomTitleView.title
        }

        override fun setBadgeDrawable(drawable: Drawable?) {
            this@CustomTitleView.badgeDrawable = drawable
        }

        override fun setSearchAffordanceColors(colors: SearchOrbView.Colors) {
            this@CustomTitleView.searchAffordanceColors = colors
        }

        override fun setTitle(titleText: CharSequence?) {
            this@CustomTitleView.title = titleText
        }

        override fun updateComponentsVisibility(flags: Int) {
            this@CustomTitleView.updateComponentsVisibility(flags)
        }
    }



    override fun getTitleViewAdapter(): TitleViewAdapter {
        return mTitleViewAdapter
    }

    private var title: CharSequence?
        get() = mTextView.text
        set(value) {
            mTextView.text = value
            updateBadgeVisibility()
        }

    private var badgeDrawable: Drawable?
        get() = mBadgeView.drawable
        set(value) {
            mBadgeView.setImageDrawable(value)
            updateBadgeVisibility()
        }

    private var searchAffordanceColors: SearchOrbView.Colors?
        get() = mSearchOrbView.orbColors
        set(value) {
            mSearchOrbView.setOrbColors(value!!)
        }

    private fun setOnSearchClickedListener(listener: OnClickListener?) {
        mHasSearchListener = listener != null
        mSearchOrbView.setOnOrbClickedListener(listener)
        updateSearchOrbViewVisibility()
    }

    private fun enableAnimation(enable: Boolean) {
        mSearchOrbView.enableOrbColorAnimation(enable && mSearchOrbView.hasFocus())
    }

    private fun updateComponentsVisibility(flags: Int) {
        this.flags = flags

        if ((flags and TitleViewAdapter.BRANDING_VIEW_VISIBLE) == TitleViewAdapter.BRANDING_VIEW_VISIBLE) {
            updateBadgeVisibility()
        } else {
            mBadgeView.visibility = View.GONE
            mTextView.visibility = View.GONE
        }
        updateSearchOrbViewVisibility()
    }

    private fun updateSearchOrbViewVisibility() {
        val visibility = if (mHasSearchListener && (flags and TitleViewAdapter.SEARCH_VIEW_VISIBLE) == TitleViewAdapter.SEARCH_VIEW_VISIBLE) {
            View.VISIBLE
        } else {
            View.INVISIBLE
        }
        mSearchOrbView.visibility = visibility
    }

    private fun updateBadgeVisibility() {
        val drawable = badgeDrawable
        if (drawable != null) {
            mBadgeView.visibility = View.VISIBLE
            mTextView.visibility = View.GONE
        } else {
            mBadgeView.visibility = View.GONE
            mTextView.visibility = View.VISIBLE
        }
    }
}
