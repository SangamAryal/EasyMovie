package com.example.easymovie.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.leanback.widget.BaseCardView
import com.example.easymovie.R

open class EpCardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BaseCardView(context, attrs, defStyleAttr) {

    private val titleView: TextView
    val mainImageView: ImageView

    init {
        inflate(context, R.layout.custom_card_view, this)
        titleView = findViewById(R.id.episode_title)
        mainImageView = findViewById(R.id.card_image)
        val gradientDrawable = ContextCompat.getDrawable(context, R.drawable.gradient_overlay)
        background = gradientDrawable
    }


    fun setTitleText(content: String) {
        titleView.text = content
    }

    fun setMainImageDimensions(width: Int, height: Int) {
        mainImageView.layoutParams.width = width
        mainImageView.layoutParams.height = height
    }
}
