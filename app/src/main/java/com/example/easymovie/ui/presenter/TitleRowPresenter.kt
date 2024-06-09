package com.example.easymovie.ui.presenter

import android.view.ViewGroup
import android.widget.TextView
import androidx.leanback.widget.Presenter

class TitleRowPresenter : Presenter() {

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
        val context = parent.context
        val textView = TextView(context)
        textView.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
        textView.setPadding(0, 16, 0, 0) // Add padding for spacing
        return ViewHolder(textView)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, item: Any?) {
        (viewHolder.view as TextView).text = item as String
    }

    override fun onUnbindViewHolder(viewHolder: ViewHolder) {
        TODO("Not yet implemented")
    }
}
