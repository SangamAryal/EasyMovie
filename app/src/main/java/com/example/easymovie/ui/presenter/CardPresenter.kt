package com.example.easymovie.ui.presenter

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.leanback.widget.Presenter
import com.bumptech.glide.Glide
import com.example.easymovie.R
import com.example.easymovie.data.model.movielist.Result
import com.example.easymovie.ui.custom.CustomBaseCardView
import com.example.easymovie.utils.Constants.IMAGE_BASE_URL
import kotlin.properties.Delegates

class CardPresenter : Presenter() {
    private var mDefaultCardImage: Drawable? = null
    private var sSelectedBackgroundColor: Int by Delegates.notNull()
    private var sDefaultBackgroundColor: Int by Delegates.notNull()

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
        Log.d(TAG, "onCreateViewHolder")

        sDefaultBackgroundColor = ContextCompat.getColor(parent.context, R.color.default_background)
        sSelectedBackgroundColor =
            ContextCompat.getColor(parent.context, R.color.selected_background)
        mDefaultCardImage = ContextCompat.getDrawable(parent.context, R.drawable.movie)

        val cardView = object : CustomBaseCardView(parent.context) {
            override fun setSelected(selected: Boolean) {
                updateCardBackgroundColor(this, selected)
                super.setSelected(selected)
            }
        }

        cardView.isFocusable = true
        cardView.isFocusableInTouchMode = true
        updateCardBackgroundColor(cardView, false)
        return ViewHolder(cardView)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, item: Any?) {
        val movie = item as Result
        val cardView = viewHolder.view as CustomBaseCardView
        val url = IMAGE_BASE_URL + movie.poster_path

        Log.d(TAG, "onBindViewHolder")
        cardView.setTitleText(movie.title)
        cardView.setContentText(movie.overview)
        cardView.setMainImageDimensions(CARD_WIDTH, CARD_HEIGHT)
        cardView.mainImageView.let {
            Glide.with(viewHolder.view.context).load(url).centerCrop().error(mDefaultCardImage)
                .into(it)
        }
    }

    override fun onUnbindViewHolder(viewHolder: ViewHolder) {
        Log.d(TAG, "onUnbindViewHolder")
        val cardView = viewHolder.view as CustomBaseCardView
        // Remove references to images so that the garbage collector can free up memory
        cardView.mainImageView.setImageDrawable(null)
    }

    private fun updateCardBackgroundColor(view: CustomBaseCardView, selected: Boolean) {
        val color = if (selected) sSelectedBackgroundColor else sDefaultBackgroundColor
        view.setBackgroundColor(color)
        view.setInfoAreaBackgroundColor(color)
    }

    companion object {
        private const val TAG = "CardPresenter"

        private const val CARD_WIDTH = 313
        private const val CARD_HEIGHT = 250
    }
}
