package com.example.easymovie.ui.custom.menu

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.leanback.widget.Presenter
import androidx.leanback.widget.Row
import androidx.leanback.widget.RowHeaderPresenter
import com.bumptech.glide.Glide
import com.example.easymovie.R

class CustomRowPresenter : Presenter() {

    override fun onCreateViewHolder(parent: ViewGroup): CustomRowViewHolder {
        val root: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.presenter_row, parent, false)

        val viewHolder = CustomRowViewHolder(root)
        return viewHolder
    }

    override fun onBindViewHolder(viewHolder: Presenter.ViewHolder, item: Any?) {
        val headerItem = if (item == null) null else (item as Row).headerItem
        val menuItem = headerItem as MenuItem
        val vh = viewHolder as CustomRowViewHolder
        vh.titleView.text = headerItem.name
        Glide.with(viewHolder.view.context).load(menuItem.icon).into(vh.imageView)
    }

    override fun onUnbindViewHolder(viewHolder: Presenter.ViewHolder) {
        val vh = viewHolder as CustomRowViewHolder
        vh.titleView.text = null
    }

    class CustomRowViewHolder(view: View) : RowHeaderPresenter.ViewHolder(view) {
        val titleView: TextView = view.findViewById(R.id.tvTitle)
        val imageView: ImageView = view.findViewById(R.id.ivItemImage)
    }
}