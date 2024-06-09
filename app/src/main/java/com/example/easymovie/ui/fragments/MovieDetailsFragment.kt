package com.example.easymovie.ui.fragments

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.leanback.app.DetailsSupportFragment
import androidx.leanback.app.DetailsSupportFragmentBackgroundController
import androidx.leanback.widget.Action
import androidx.leanback.widget.ArrayObjectAdapter
import androidx.leanback.widget.ClassPresenterSelector
import androidx.leanback.widget.DetailsOverviewRow
import androidx.leanback.widget.FullWidthDetailsOverviewRowPresenter
import androidx.leanback.widget.FullWidthDetailsOverviewSharedElementHelper
import androidx.leanback.widget.HeaderItem
import androidx.leanback.widget.ListRow
import androidx.leanback.widget.ListRowPresenter
import androidx.leanback.widget.OnActionClickedListener
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.example.easymovie.R
import com.example.easymovie.data.model.Cast
import com.example.easymovie.data.model.movielist.Result
import com.example.easymovie.ui.activity.DetailsActivity
import com.example.easymovie.ui.activity.MainActivity
import com.example.easymovie.ui.activity.PlaybackActivity
import com.example.easymovie.ui.presenter.CastCrewPresenter
import com.example.easymovie.ui.presenter.DetailsDescPresenter
import com.example.easymovie.utils.Constants.IMAGE_BASE_URL

class MovieDetailsFragment : DetailsSupportFragment() {
    private var mSelectedMovie: Result? = null

    private lateinit var mDetailsBackground: DetailsSupportFragmentBackgroundController
    private lateinit var mPresenterSelector: ClassPresenterSelector
    private lateinit var mAdapter: ArrayObjectAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate DetailsFragment")
        super.onCreate(savedInstanceState)
        mDetailsBackground = DetailsSupportFragmentBackgroundController(this)

        mSelectedMovie =
            requireActivity().intent.getSerializableExtra(DetailsActivity.MOVIE) as Result
        if (mSelectedMovie != null) {
            mPresenterSelector = ClassPresenterSelector()
            mAdapter = ArrayObjectAdapter(mPresenterSelector)
            setupDetailsOverviewRow()
            setupDetailsOverviewRowPresenter()
            setupCastPresenter()
            adapter = mAdapter
            initializeBackground(mSelectedMovie)
        } else {
            val intent = Intent(requireActivity(), MainActivity::class.java)
            startActivity(intent)
        }
    }

    fun handleKeyEvent(keyCode: Int, event: KeyEvent): Boolean {
        if (event.action == KeyEvent.ACTION_DOWN) {
            when (keyCode) {
                KeyEvent.KEYCODE_DPAD_UP -> {
                    val newEvent = KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DPAD_DOWN)
                    requireActivity().onKeyDown(newEvent.keyCode, newEvent)
                    return true
                }
            }
        }
        return false
    }

    private fun initializeBackground(movie: Result?) {
        val uri = IMAGE_BASE_URL + movie?.backdrop_path
        mDetailsBackground.enableParallax()
        Glide.with(requireActivity()).asBitmap().centerCrop().error(R.drawable.default_background)
            .load(uri).into<SimpleTarget<Bitmap>>(object : SimpleTarget<Bitmap>() {
                override fun onResourceReady(bitmap: Bitmap, transition: Transition<in Bitmap>?) {
                    mDetailsBackground.coverBitmap = bitmap
                    mAdapter.notifyArrayItemRangeChanged(0, mAdapter.size())
                }
            })
    }

    private fun setupDetailsOverviewRow() {
        Log.d(TAG, "doInBackground: " + mSelectedMovie?.toString())
        val row = mSelectedMovie?.let { DetailsOverviewRow(it) }
        if (row != null) {
            row.imageDrawable =
                ContextCompat.getDrawable(requireActivity(), R.drawable.default_background)
        }
        val width = convertDpToPixel(requireActivity(), DETAIL_THUMB_WIDTH)
        val height = convertDpToPixel(requireActivity(), DETAIL_THUMB_HEIGHT)
        val uri = IMAGE_BASE_URL + mSelectedMovie?.poster_path
        Glide.with(requireActivity()).load(uri).centerCrop().error(R.drawable.default_background)
            .into<SimpleTarget<Drawable>>(object : SimpleTarget<Drawable>(width, height) {
                override fun onResourceReady(
                    drawable: Drawable, transition: Transition<in Drawable>?
                ) {
                    Log.d(TAG, "details overview card image url ready: $drawable")
                    if (row != null) {
                        row.imageDrawable = drawable
                    }
                    mAdapter.notifyArrayItemRangeChanged(0, mAdapter.size())
                }
            })

        val actionAdapter = ArrayObjectAdapter()
        actionAdapter.add(
            Action(
                ACTION_WATCH_MOVIE,
                resources.getString(R.string.watch_trailer_1),
                resources.getString(R.string.watch_trailer_2)
            )
        )

        if (row != null) {
            row.actionsAdapter = actionAdapter
        }

        if (row != null) {
            mAdapter.add(row)
        }
    }

    private fun setupDetailsOverviewRowPresenter() {
        // Set detail background.
        val detailsPresenter = FullWidthDetailsOverviewRowPresenter(DetailsDescPresenter())
        detailsPresenter.backgroundColor =
            ContextCompat.getColor(requireActivity(), R.color.selected_background)

        // Hook up transition element.
        val sharedElementHelper = FullWidthDetailsOverviewSharedElementHelper()
        sharedElementHelper.setSharedElementEnterTransition(
            activity, DetailsActivity.SHARED_ELEMENT_NAME
        )
        detailsPresenter.setListener(sharedElementHelper)
        detailsPresenter.isParticipatingEntranceTransition = true

        detailsPresenter.onActionClickedListener = OnActionClickedListener { action ->
            if (action.id == ACTION_WATCH_MOVIE) {
                val intent = Intent(requireActivity(), PlaybackActivity::class.java)
                intent.putExtra(DetailsActivity.MOVIE, mSelectedMovie)
                startActivity(intent)
            } else {
                Toast.makeText(activity, "Yet to be Implemented", Toast.LENGTH_SHORT).show()
            }
        }
        mPresenterSelector.addClassPresenter(DetailsOverviewRow::class.java, detailsPresenter)
    }


    private fun setupCastPresenter() {
        val castCrewList = listOf(
            Cast(
                "Saitama",
                "https://static.wikia.nocookie.net/onepunchman/images/8/81/Saitama_Anime_Profile.png/revision/latest?cb=20161002154538"
            ),
            Cast(
                "Kamado Tanjiro",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSEjJEYfgrbvwEhD2tqkHBG62q0xJBjX4oOew&s"
            ),
            Cast(
                "Satoru Gojo",
                "https://images-wixmp-ed30a86b8c4ca887773594c2.wixmp.com/f/2db19446-b492-4773-be60-ba81850f91f9/dgfzgxt-18499a40-edea-43da-8c15-603f6e893244.jpg?token=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1cm46YXBwOjdlMGQxODg5ODIyNjQzNzNhNWYwZDQxNWVhMGQyNmUwIiwiaXNzIjoidXJuOmFwcDo3ZTBkMTg4OTgyMjY0MzczYTVmMGQ0MTVlYTBkMjZlMCIsIm9iaiI6W1t7InBhdGgiOiJcL2ZcLzJkYjE5NDQ2LWI0OTItNDc3My1iZTYwLWJhODE4NTBmOTFmOVwvZGdmemd4dC0xODQ5OWE0MC1lZGVhLTQzZGEtOGMxNS02MDNmNmU4OTMyNDQuanBnIn1dXSwiYXVkIjpbInVybjpzZXJ2aWNlOmZpbGUuZG93bmxvYWQiXX0.S9JCnHluP5BriqxT18KeozUkLa1wkOa7V7BfNh0rDXY"
            ),
            Cast(
                "Kyojuro Rengoku",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRgQE-UzteaHc1S5aSK_Tb6k4KolVi_mZiivQ&s"
            ),
            Cast(
                "Saitama",
                "https://static.wikia.nocookie.net/onepunchman/images/8/81/Saitama_Anime_Profile.png/revision/latest?cb=20161002154538"
            ),
            Cast(
                "Kamado Tanjiro",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSEjJEYfgrbvwEhD2tqkHBG62q0xJBjX4oOew&s"
            ),
            Cast(
                "Satoru Gojo",
                "https://images-wixmp-ed30a86b8c4ca887773594c2.wixmp.com/f/2db19446-b492-4773-be60-ba81850f91f9/dgfzgxt-18499a40-edea-43da-8c15-603f6e893244.jpg?token=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1cm46YXBwOjdlMGQxODg5ODIyNjQzNzNhNWYwZDQxNWVhMGQyNmUwIiwiaXNzIjoidXJuOmFwcDo3ZTBkMTg4OTgyMjY0MzczYTVmMGQ0MTVlYTBkMjZlMCIsIm9iaiI6W1t7InBhdGgiOiJcL2ZcLzJkYjE5NDQ2LWI0OTItNDc3My1iZTYwLWJhODE4NTBmOTFmOVwvZGdmemd4dC0xODQ5OWE0MC1lZGVhLTQzZGEtOGMxNS02MDNmNmU4OTMyNDQuanBnIn1dXSwiYXVkIjpbInVybjpzZXJ2aWNlOmZpbGUuZG93bmxvYWQiXX0.S9JCnHluP5BriqxT18KeozUkLa1wkOa7V7BfNh0rDXY"
            ),
            Cast(
                "Kyojuro Rengoku",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRgQE-UzteaHc1S5aSK_Tb6k4KolVi_mZiivQ&s"
            ),
            Cast(
                "Saitama",
                "https://static.wikia.nocookie.net/onepunchman/images/8/81/Saitama_Anime_Profile.png/revision/latest?cb=20161002154538"
            ),
            Cast(
                "Kamado Tanjiro",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSEjJEYfgrbvwEhD2tqkHBG62q0xJBjX4oOew&s"
            ),
            Cast(
                "Satoru Gojo",
                "https://images-wixmp-ed30a86b8c4ca887773594c2.wixmp.com/f/2db19446-b492-4773-be60-ba81850f91f9/dgfzgxt-18499a40-edea-43da-8c15-603f6e893244.jpg?token=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1cm46YXBwOjdlMGQxODg5ODIyNjQzNzNhNWYwZDQxNWVhMGQyNmUwIiwiaXNzIjoidXJuOmFwcDo3ZTBkMTg4OTgyMjY0MzczYTVmMGQ0MTVlYTBkMjZlMCIsIm9iaiI6W1t7InBhdGgiOiJcL2ZcLzJkYjE5NDQ2LWI0OTItNDc3My1iZTYwLWJhODE4NTBmOTFmOVwvZGdmemd4dC0xODQ5OWE0MC1lZGVhLTQzZGEtOGMxNS02MDNmNmU4OTMyNDQuanBnIn1dXSwiYXVkIjpbInVybjpzZXJ2aWNlOmZpbGUuZG93bmxvYWQiXX0.S9JCnHluP5BriqxT18KeozUkLa1wkOa7V7BfNh0rDXY"
            ),
            Cast(
                "Kyojuro Rengoku",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRgQE-UzteaHc1S5aSK_Tb6k4KolVi_mZiivQ&s"
            ),
            Cast(
                "Saitama",
                "https://static.wikia.nocookie.net/onepunchman/images/8/81/Saitama_Anime_Profile.png/revision/latest?cb=20161002154538"
            ),
            Cast(
                "Kamado Tanjiro",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSEjJEYfgrbvwEhD2tqkHBG62q0xJBjX4oOew&s"
            ),
            Cast(
                "Satoru Gojo",
                "https://images-wixmp-ed30a86b8c4ca887773594c2.wixmp.com/f/2db19446-b492-4773-be60-ba81850f91f9/dgfzgxt-18499a40-edea-43da-8c15-603f6e893244.jpg?token=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1cm46YXBwOjdlMGQxODg5ODIyNjQzNzNhNWYwZDQxNWVhMGQyNmUwIiwiaXNzIjoidXJuOmFwcDo3ZTBkMTg4OTgyMjY0MzczYTVmMGQ0MTVlYTBkMjZlMCIsIm9iaiI6W1t7InBhdGgiOiJcL2ZcLzJkYjE5NDQ2LWI0OTItNDc3My1iZTYwLWJhODE4NTBmOTFmOVwvZGdmemd4dC0xODQ5OWE0MC1lZGVhLTQzZGEtOGMxNS02MDNmNmU4OTMyNDQuanBnIn1dXSwiYXVkIjpbInVybjpzZXJ2aWNlOmZpbGUuZG93bmxvYWQiXX0.S9JCnHluP5BriqxT18KeozUkLa1wkOa7V7BfNh0rDXY"
            ),
            Cast(
                "Kyojuro Rengoku",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRgQE-UzteaHc1S5aSK_Tb6k4KolVi_mZiivQ&s"
            ),
        )

        val castCrewAdapter = ArrayObjectAdapter(CastCrewPresenter())
        castCrewAdapter.addAll(0, castCrewList)

        val header = HeaderItem(1, "Cast & Crew")
        mAdapter.add(ListRow(header, castCrewAdapter))
        mPresenterSelector.addClassPresenter(ListRow::class.java, ListRowPresenter())
    }

    private fun convertDpToPixel(context: Context, dp: Int): Int {
        val density = context.applicationContext.resources.displayMetrics.density
        return Math.round(dp.toFloat() * density)
    }

    companion object {
        private const val TAG = "MovieDetailsFragments"
        private const val ACTION_WATCH_MOVIE = 1L
        private const val DETAIL_THUMB_WIDTH = 274
        private const val DETAIL_THUMB_HEIGHT = 274
    }
}
