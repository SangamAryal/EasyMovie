package com.example.easymovie.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.leanback.app.BrowseSupportFragment
import androidx.leanback.app.RowsSupportFragment
import androidx.leanback.widget.*
import com.example.easymovie.R
import com.example.easymovie.data.model.Card
import com.example.easymovie.data.model.CardRow
import com.example.easymovie.ui.activity.GuidanceActivity
import com.example.easymovie.ui.activity.SettingsActivity
import com.example.easymovie.ui.presenter.SettingIconPresenter
import com.google.gson.Gson

class SettingsFragment : RowsSupportFragment(), BrowseSupportFragment.MainFragmentAdapterProvider {

    private val mRowsAdapter: ArrayObjectAdapter

    init {
        val listRowPresenter = ListRowPresenter().apply {
            setNumRows(2)
        }
        mRowsAdapter = ArrayObjectAdapter(listRowPresenter)
        adapter = mRowsAdapter
    }

    override fun getMainFragmentAdapter(): BrowseSupportFragment.MainFragmentAdapter<*> {
        return BrowseSupportFragment.MainFragmentAdapter(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.setPadding(5, 100, 5, 0)
        setupEventListeners()
        loadData()
    }

    private fun setupEventListeners() {
        onItemViewClickedListener = ItemViewClickedListener()
    }

    private fun loadData() {
        if (isAdded) {
            val json = readJsonFile(R.raw.icon_example)
            val cardRow = Gson().fromJson(json, CardRow::class.java)
            mRowsAdapter.add(createCardRow(cardRow))
        }
    }

    private fun createCardRow(cardRow: CardRow): ListRow {
        val iconCardPresenter = SettingIconPresenter()
        val adapter = ArrayObjectAdapter(iconCardPresenter)

        cardRow.cards.forEach { card ->
            adapter.add(card)
        }

        // No header item provided
        return ListRow(adapter)
    }


    private fun readJsonFile(resourceId: Int): String {
        val inputStream = resources.openRawResource(resourceId)
        return inputStream.bufferedReader().use { it.readText() }
    }

    private inner class ItemViewClickedListener : OnItemViewClickedListener {
        override fun onItemClicked(
            itemViewHolder: Presenter.ViewHolder?,
            item: Any?,
            rowViewHolder: RowPresenter.ViewHolder?,
            row: Row?
        ) {
            if (item is Card && item.title == "Settings") {
                val intent = Intent(activity, SettingsActivity::class.java)
                startActivity(intent)
            }
            if (item is Card && item.title == "Time") {
                val intent = Intent(activity, GuidanceActivity::class.java)
                startActivity(intent)
            }

        }
    }
}
