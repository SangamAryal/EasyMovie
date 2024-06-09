package com.example.easymovie.ui.presenter

import androidx.leanback.widget.AbstractDetailsDescriptionPresenter
import com.example.easymovie.ui.custom.VideoMediaPlayerGlue

class MyCustomDescriptionPresenter : AbstractDetailsDescriptionPresenter() {
    override fun onBindDescription(vh: ViewHolder, item: Any) {
        if (item is VideoMediaPlayerGlue<*>) {
            val result = item.getResult()
            vh.title.text = result.title
            vh.body.text = result.overview
        } else {
            vh.title.text = "Title unavailable"
            vh.body.text = "Description unavailable"
        }
    }
}