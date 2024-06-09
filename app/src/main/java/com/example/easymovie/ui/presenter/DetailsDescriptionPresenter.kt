package com.example.easymovie.ui.presenter

import androidx.leanback.widget.AbstractDetailsDescriptionPresenter
import com.example.easymovie.data.model.movielist.Result

class DetailsDescriptionPresenter:AbstractDetailsDescriptionPresenter() {
    override fun onBindDescription(viewHolder: ViewHolder, item: Any) {
        val movie = item as Result

        viewHolder.title.text = movie.title
        viewHolder.subtitle.text = movie.original_title
        viewHolder.body.text = movie.overview

    }
}