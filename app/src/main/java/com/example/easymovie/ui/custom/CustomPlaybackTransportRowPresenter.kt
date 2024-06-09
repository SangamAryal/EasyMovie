package com.example.easymovie.ui.custom

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.leanback.widget.PlaybackRowPresenter
import androidx.leanback.widget.PlaybackTransportRowPresenter
import com.example.easymovie.R

class CustomPlaybackTransportRowPresenter : PlaybackTransportRowPresenter() {
    override fun createRowViewHolder(parent: ViewGroup): PlaybackRowPresenter.ViewHolder {
        val customControlsRowView =
            LayoutInflater.from(parent.context).inflate(R.layout.custom_seekbar, parent, false)
        return ViewHolder(customControlsRowView)
    }
}
