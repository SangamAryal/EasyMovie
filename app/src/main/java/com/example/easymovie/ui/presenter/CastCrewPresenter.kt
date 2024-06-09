package com.example.easymovie.ui.presenter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.leanback.widget.Presenter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.easymovie.R
import com.example.easymovie.data.model.Cast
import com.example.easymovie.databinding.CastItemBinding


class CastCrewPresenter : Presenter() {

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cast_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, item: Any?) {
        val castCrewMember = item as Cast
        val castView = viewHolder.view
        val castCrewName: TextView = castView.findViewById(R.id.castCrewName)
        val castCrewImage: ImageView = castView.findViewById(R.id.castCrewImage)

        castCrewName.text = castCrewMember.name
        Glide.with(viewHolder.view.context)
            .load(castCrewMember.profilePath)
            .apply(RequestOptions.circleCropTransform())
            .error(R.drawable.default_background)
            .into(castCrewImage)

        // Handle focus change to highlight focused item
        castCrewImage.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                castCrewImage.setBackgroundResource(R.drawable.focused_border)
                castCrewImage.elevation = 10f
            } else {
                castCrewImage.setBackgroundResource(0)  // Remove the background
                castCrewImage.elevation = 0f
            }
        }
    }


    override fun onUnbindViewHolder(viewHolder: ViewHolder) {
        // Clean up resources if needed
    }
}
