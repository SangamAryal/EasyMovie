package com.example.easymovie.ui.presenter

import android.graphics.Rect
import android.text.Layout.Alignment
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.setPadding
import androidx.leanback.widget.ImageCardView
import androidx.leanback.widget.Presenter
import com.bumptech.glide.Glide
import com.example.easymovie.R
import com.example.easymovie.data.model.Card
import kotlin.properties.Delegates

class SettingIconPresenter : Presenter() {
    private var sSelectedBackgroundColor: Int by Delegates.notNull()
    private var sDefaultBackgroundColor: Int by Delegates.notNull()

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
        sDefaultBackgroundColor =
            ContextCompat.getColor(parent.context, R.color.settings_card_background)
        sSelectedBackgroundColor =
            ContextCompat.getColor(parent.context, R.color.settings_card_background_focussed)

        val cardView = object : ImageCardView(parent.context) {
            override fun setSelected(selected: Boolean) {
                super.setSelected(selected)
//                updateCardBackgroundColor(this, selected)

            }

            override fun setActivated(activated: Boolean) {
                super.setActivated(activated)
//                                updateCardBackgroundColor(this, activated)

            }

            override fun onFocusChanged(
                gainFocus: Boolean,
                direction: Int,
                previouslyFocusedRect: Rect?
            ) {
                super.onFocusChanged(gainFocus, direction, previouslyFocusedRect)
                if (gainFocus) {
                    setBackgroundColor(
                        ContextCompat.getColor(
                            context,
                            R.color.settings_card_background_focussed
                        )
                    )
                } else {
                    setBackgroundColor(
                        ContextCompat.getColor(
                            context,
                            R.color.settings_card_background
                        )
                    )
                }
            }
        }
        cardView.setBackgroundColor(
            ContextCompat.getColor(
                parent.context,
                R.color.settings_card_background
            )
        )
        cardView.isFocusable = true
        cardView.isFocusableInTouchMode = true
        return ViewHolder(cardView)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, item: Any?) {
        val card = item as Card
        val cardView = viewHolder.view as ImageCardView
        cardView.titleText = card.title
        cardView.contentText = ""
        cardView.setMainImageDimensions(
            CARD_WIDTH, CARD_HEIGHT
        )
        cardView.mainImageView?.adjustViewBounds ?: true
        cardView.mainImageView?.setPadding(40)


        // Load the local image resource
        val resourceId = cardView.context.resources.getIdentifier(
            card.localImageResource, "drawable", cardView.context.packageName
        )
        cardView.mainImageView?.let {
            Glide.with(viewHolder.view.context)
                .load(resourceId)
                .fitCenter()
                .into(it)
        }
    }

    override fun onUnbindViewHolder(viewHolder: ViewHolder) {
        val cardView = viewHolder.view as ImageCardView
        cardView.mainImageView?.setImageDrawable(null)
    }

    private fun updateCardBackgroundColor(view: ImageCardView, selected: Boolean) {
        val color = if (selected) sSelectedBackgroundColor else sDefaultBackgroundColor
        view.setBackgroundColor(color)
        view.setInfoAreaBackgroundColor(color)
    }

    companion object {
        private const val TAG = "CustomImageCardPresenter"
        private const val CARD_WIDTH = 140
        private const val CARD_HEIGHT = 180
    }
}
