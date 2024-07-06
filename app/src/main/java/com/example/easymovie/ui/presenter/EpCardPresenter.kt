package com.example.easymovie.ui.presenter

import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.util.Log
import android.view.KeyEvent
import android.view.ViewGroup
import androidx.leanback.widget.Presenter
import com.bumptech.glide.Glide
import com.example.easymovie.data.model.movielist.Result
import com.example.easymovie.ui.activity.DetailsActivity
import com.example.easymovie.ui.custom.EpCardView
import com.example.easymovie.utils.Constants


class EpCardPresenter : Presenter() {

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
        Log.d(TAG, "onCreateViewHolder")

        val cardView = object : EpCardView(parent.context) {}

        cardView.isFocusable = true
        cardView.isFocusableInTouchMode = true
        val layoutParams = ViewGroup.MarginLayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        layoutParams.setMargins(MARGIN_LEFT, MARGIN_TOP, MARGIN_RIGHT, MARGIN_BOTTOM)
        cardView.layoutParams = layoutParams

        cardView.setOnKeyListener { v, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN) {
                when (keyCode) {
                    KeyEvent.KEYCODE_DPAD_UP -> {
                        (v.context as? DetailsActivity)?.let { activity ->
                            activity.getLastFocusedView()?.requestFocus()
                        }
                        true
                    }

                    else -> false
                }
            } else {
                false
            }
        }


        return ViewHolder(cardView)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, item: Any?) {
        val movie = item as Result
        val cardView = viewHolder.view as EpCardView
        val url = Constants.IMAGE_BASE_URL + movie.poster_path



        Log.d(TAG, "onBindViewHolder")
        cardView.setTitleText(movie.title)
        cardView.setMainImageDimensions(CARD_WIDTH, CARD_HEIGHT)
        cardView.mainImageView.let {
            Glide.with(viewHolder.view.context).load(url).centerCrop()
                .into(it)
        }
//        val color = Color.argb(153, 0, 0, 0)
//        val colorFilter = PorterDuffColorFilter(color, PorterDuff.Mode.SRC_ATOP)
//        cardView.mainImageView.colorFilter = colorFilter

    }

    override fun onUnbindViewHolder(viewHolder: ViewHolder) {
        Log.d(TAG, "onUnbindViewHolder")
        val cardView = viewHolder.view as EpCardView
        cardView.mainImageView.setImageDrawable(null)
    }

    companion object {
        private const val TAG = "EpCardPresenter"

        private const val CARD_WIDTH = 272
        private const val CARD_HEIGHT = 170

        private const val MARGIN_LEFT = 5
        private const val MARGIN_TOP = 5
        private const val MARGIN_RIGHT = 5
        private const val MARGIN_BOTTOM = 5
    }
}