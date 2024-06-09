package com.example.easymovie.ui.custom

import android.app.Activity
import android.widget.Toast
import androidx.leanback.media.PlaybackTransportControlGlue
import androidx.leanback.media.PlayerAdapter
import androidx.leanback.widget.Action
import androidx.leanback.widget.ArrayObjectAdapter
import androidx.leanback.widget.PlaybackControlsRow.ClosedCaptioningAction
import androidx.leanback.widget.PlaybackControlsRow.FastForwardAction
import androidx.leanback.widget.PlaybackControlsRow.MultiAction
import androidx.leanback.widget.PlaybackControlsRow.RepeatAction
import androidx.leanback.widget.PlaybackControlsRow.RewindAction
import androidx.leanback.widget.PlaybackControlsRow.ShuffleAction
import androidx.leanback.widget.PlaybackControlsRow.SkipNextAction
import androidx.leanback.widget.PlaybackControlsRow.SkipPreviousAction
import androidx.leanback.widget.PlaybackControlsRow.ThumbsUpAction
import androidx.leanback.widget.PlaybackRowPresenter
import androidx.leanback.widget.PlaybackTransportRowPresenter
import com.example.easymovie.data.model.movielist.Result
import com.example.easymovie.ui.presenter.MyCustomDescriptionPresenter

class VideoMediaPlayerGlue<T : PlayerAdapter>(
    context: Activity,
    impl: T,
    private val result: Result
) :
    PlaybackTransportControlGlue<T>(context, impl) {

    private val mThumbsUpAction = ThumbsUpAction(context).apply {
        index = ThumbsUpAction.INDEX_OUTLINE
    }
    private val mShuffleAction = ShuffleAction(context).apply {
        index = ShuffleAction.INDEX_OFF
    }
    private val mClosedCaptioningAction = ClosedCaptioningAction(context)
    private val mFastForwardAction = FastForwardAction(context)
    private val mRewindAction = RewindAction(context)
    private val mSkipNextAction = SkipNextAction(context)
    private val mSkipPreviousAction = SkipPreviousAction(context)
    private val mRepeatAction = RepeatAction(context)

    override fun onCreatePrimaryActions(adapter: ArrayObjectAdapter) {
        adapter.apply {
            add(mSkipPreviousAction)
            add(mRewindAction)
            super.onCreatePrimaryActions(adapter)
            add(mFastForwardAction)
            add(mSkipNextAction)
        }
    }

    override fun onCreateSecondaryActions(adapter: ArrayObjectAdapter) {
        super.onCreateSecondaryActions(adapter)
        adapter.apply {
            add(mThumbsUpAction)
            add(mShuffleAction)
            add(mClosedCaptioningAction)
        }
    }

    override fun onActionClicked(action: Action) {
        when (action) {
            mRewindAction -> rewind()
            mFastForwardAction -> fastForward()
            mShuffleAction, mThumbsUpAction, mRepeatAction, mClosedCaptioningAction -> dispatchAction(
                action
            )

            else -> super.onActionClicked(action)
        }
    }

//    override fun onCreateRowPresenter(): PlaybackRowPresenter {
//        return CustomPlaybackTransportRowPresenter().apply {
//            setDescriptionPresenter(MyCustomDescriptionPresenter())
//        }
//    }


    fun getResult(): Result {
        return result
    }


    private fun dispatchAction(action: Action) {
        Toast.makeText(context, action.toString(), Toast.LENGTH_SHORT).show()
        if (action is MultiAction) {
            action.nextIndex()
            notifyActionChanged(action)
        } else {
            super.onActionClicked(action)
        }
    }

    private fun notifyActionChanged(action: MultiAction) {
        var index = getPrimaryActionsAdapter()?.indexOf(action) ?: -1
        if (index >= 0) {
            getPrimaryActionsAdapter()?.notifyArrayItemRangeChanged(index, 1)
        } else {
            index = getSecondaryActionsAdapter()?.indexOf(action) ?: -1
            if (index >= 0) {
                getSecondaryActionsAdapter()?.notifyArrayItemRangeChanged(index, 1)
            }
        }
    }

    private fun getPrimaryActionsAdapter(): ArrayObjectAdapter? {
        return controlsRow?.primaryActionsAdapter as? ArrayObjectAdapter
    }

    private fun getSecondaryActionsAdapter(): ArrayObjectAdapter? {
        return controlsRow?.secondaryActionsAdapter as? ArrayObjectAdapter
    }

    private fun rewind() {
        playerAdapter?.seekTo(
            (playerAdapter?.currentPosition ?: 0) - 10000
        )
    }

    private fun fastForward() {
        playerAdapter?.seekTo(
            (playerAdapter?.currentPosition ?: 0) + 10000
        )
    }

    override fun onCreateRowPresenter(): PlaybackRowPresenter {
        return super.onCreateRowPresenter().apply {
            (this as? PlaybackTransportRowPresenter)?.setDescriptionPresenter(
                MyCustomDescriptionPresenter()
            )
        }
    }


}



