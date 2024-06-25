package com.example.easymovie.ui.fragments.adapter

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.easymovie.R

class TabItemAdapter(
    private val context: Context,
    private val itemList: List<String>,
    private val onTabClick: (Int) -> Unit
) : RecyclerView.Adapter<TabItemAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.tab_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(itemList[position], position)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val itemText: TextView = itemView.findViewById(R.id.item_text)

        fun bind(item: String, position: Int) {
            itemText.text = item
            itemText.setOnClickListener {
                onTabClick(position)
            }

            itemText.setOnFocusChangeListener { v, hasFocus ->
                if (hasFocus) {
                    v.elevation = 8.0f
                    if (v is TextView) {
                        v.setTypeface(null, Typeface.BOLD)
                        v.setTextColor(Color.WHITE)
                    }
                } else {
                    v.elevation = 0f
                    if (v is TextView) {
                        v.setTypeface(null, Typeface.NORMAL)
                        v.setTextColor(ContextCompat.getColor(context, R.color.bar))
                    }
                }
            }
        }
    }
}