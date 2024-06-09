package com.example.easymovie.ui.presenter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.leanback.widget.AbstractDetailsDescriptionPresenter
import com.bumptech.glide.Glide
import com.example.easymovie.data.model.movielist.Result
import com.example.easymovie.R
import com.example.easymovie.utils.Constants.IMAGE_BASE_URL

class DetailsDescPresenter : AbstractDetailsDescriptionPresenter() {

    override fun onBindDescription(viewHolder: ViewHolder, item: Any) {
        // Inflate the view if not already done
        val view = viewHolder.view

        if (view.findViewById<TextView>(R.id.title_movie) == null) {
            val inflater = LayoutInflater.from(view.context)
            val parent = view as ViewGroup
            inflater.inflate(R.layout.custom_full_width_details_overview, parent, true)
        }

        // Bind the data
        val movie = item as Result
        val title = view.findViewById<TextView>(R.id.title_movie)
        val anotherTitle = view.findViewById<TextView>(R.id.another_title)
        val description = view.findViewById<TextView>(R.id.description_movie)
        val movieImage = view.findViewById<ImageView>(R.id.movieImage)

        title.text = movie.title
        anotherTitle.text = movie.original_title
        description.text = movie.overview

        val url = IMAGE_BASE_URL + movie.poster_path
        Glide.with(view.context)
            .load(url)
            .override(100, 100)
            .into(movieImage)

    }
}
