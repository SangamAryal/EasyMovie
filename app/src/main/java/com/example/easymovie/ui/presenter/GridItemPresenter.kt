package com.example.easymovie.ui.presenter

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.ContextThemeWrapper
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.leanback.widget.ImageCardView
import androidx.leanback.widget.Presenter
import com.bumptech.glide.Glide
import com.example.easymovie.R
import com.example.easymovie.data.model.movielist.Result
import com.example.easymovie.utils.Constants.IMAGE_BASE_URL
import kotlin.properties.Delegates


class GridItemPresenter : Presenter() {
    private var mDefaultCardImage: Drawable? = null
    private var sSelectedBackgroundColor: Int by Delegates.notNull()
    private var sDefaultBackgroundColor: Int by Delegates.notNull()

    override fun onCreateViewHolder(parent: ViewGroup): Presenter.ViewHolder {
        Log.d(TAG, "onCreateViewHolder")

        sDefaultBackgroundColor = ContextCompat.getColor(parent.context, R.color.default_background)
        sSelectedBackgroundColor = ContextCompat.getColor(parent.context, R.color.selected_background)
        mDefaultCardImage = ContextCompat.getDrawable(parent.context, R.drawable.movie)

        val cardView = object : ImageCardView(
            ContextThemeWrapper(
                parent.context,
                R.style.CustomImageCardTheme
            )
        ) {
            override fun setSelected(selected: Boolean) {
                super.setSelected(selected)
                updateCardBackgroundColor(this, selected)
            }

            override fun setActivated(activated: Boolean) {
                super.setActivated(activated)
                updateCardBackgroundColor(this, activated)
            }
        }

        cardView.isFocusable = true
        cardView.isFocusableInTouchMode = true
        updateCardBackgroundColor(cardView, false)

        return Presenter.ViewHolder(cardView)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, item: Any?) {
        val movie = item as Result
        val cardView = viewHolder.view as ImageCardView
        val url = IMAGE_BASE_URL + movie.poster_path

        Log.d(TAG, "onBindViewHolder")
        cardView.titleText = movie.title
        cardView.contentText = movie.overview
        cardView.setMainImageDimensions(CARD_WIDTH, CARD_HEIGHT)
        cardView.mainImageView?.let {
            Glide.with(viewHolder.view.context)
                .load(url)
                .centerCrop()
                .error(mDefaultCardImage)
                .into(it)
        }
        Log.d(TAG, "Card bound with movie: ${movie.title}")
    }

    override fun onUnbindViewHolder(viewHolder: Presenter.ViewHolder) {
        Log.d(TAG, "onUnbindViewHolder")
        val cardView = viewHolder.view as ImageCardView
        // Remove references to images so that the garbage collector can free up memory
        cardView.badgeImage = null
        cardView.mainImage = null
    }

    private fun updateCardBackgroundColor(view: ImageCardView, selected: Boolean) {
        val color = if (selected) sSelectedBackgroundColor else sDefaultBackgroundColor
        // Both background colors should be set because the view's background is temporarily visible
        // during animations.
        view.setBackgroundColor(color)
//        view.setInfoAreaBackgroundColor(color)
        Log.d(TAG, "Card background color updated to: ${if (selected) "selected" else "default"}")
    }

    companion object {
        private const val TAG = "GridItemPresenter"
        private const val CARD_WIDTH = 300
        private const val CARD_HEIGHT = 350
    }
}
