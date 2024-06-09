package com.example.easymovie.ui.custom

import android.content.Context
import android.util.AttributeSet
import androidx.leanback.widget.BaseCardView
import android.widget.ImageView
import android.widget.TextView
import com.example.easymovie.R

open class CustomBaseCardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BaseCardView(context, attrs, defStyleAttr) {

    private val titleView: TextView
    private val contentView: TextView
    val mainImageView: ImageView

    init {
        inflate(context, R.layout.card_view, this)
        titleView = findViewById(R.id.movie_title)
        contentView = findViewById(R.id.movie_content)
        mainImageView = findViewById(R.id.movie_image)
    }

    fun setInfoAreaBackgroundColor(color: Int) {
        titleView.setBackgroundColor(color)
        contentView.setBackgroundColor(color)
    }

    fun setTitleText(title: String) {
        titleView.text = title
    }

    fun setContentText(content: String) {
        contentView.text = content
    }

    fun setMainImageDimensions(width: Int, height: Int) {
        mainImageView.layoutParams.width = width
        mainImageView.layoutParams.height = height
    }
}
