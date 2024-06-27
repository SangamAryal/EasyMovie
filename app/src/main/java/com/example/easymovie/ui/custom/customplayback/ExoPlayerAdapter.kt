package com.example.easymovie.ui.custom.customplayback

import android.content.Context
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.SurfaceHolder
import androidx.leanback.media.PlaybackGlueHost
import androidx.leanback.media.PlayerAdapter
import androidx.leanback.media.SurfaceHolderGlueHost
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer


class ExoPlayerAdapter(context: Context) : PlayerAdapter() {

    private val exoPlayer: ExoPlayer = ExoPlayer.Builder(context).build()
    var mSurfaceHolderGlueHost: SurfaceHolderGlueHost? = null
    private var mHasDisplay = false
    private var mInitialized = false
    var mBufferingStart: Boolean = false
    private val mHandler = Handler(Looper.getMainLooper())
    private val mUpdatePositionRunnable = object : Runnable {
        override fun run() {
            if (mInitialized && isPlaying()) {
                callback?.onCurrentPositionChanged(this@ExoPlayerAdapter)
                mHandler.postDelayed(this, getUpdatePeriod())
            }
        }
    }


    init {
        exoPlayer.addListener(object : Player.Listener {
            override fun onPlayerError(error: PlaybackException) {
                Log.e("ExoPlayerAdapter", "Player error: ${error.message}")
            }

            override fun onPlaybackStateChanged(playbackState: Int) {
                mBufferingStart = false
                Log.d("ExoPlayerAdapter", "Playback state changed: $playbackState")
                if (playbackState == ExoPlayer.STATE_READY && !mInitialized) {
                    mInitialized = true;
                    if (mSurfaceHolderGlueHost == null || mHasDisplay) {
                        callback?.onPreparedStateChanged(this@ExoPlayerAdapter);
                    }
                } else if (playbackState == ExoPlayer.STATE_BUFFERING) {
                    mBufferingStart = true;
                } else if (playbackState == ExoPlayer.STATE_ENDED) {
                    callback?.onPlayStateChanged(this@ExoPlayerAdapter);
                    callback?.onPlayCompleted(this@ExoPlayerAdapter);
                }
                notifyBufferStartEnd()
            }

            override fun onIsPlayingChanged(isPlaying: Boolean) {
                Log.d("ExoPlayerAdapter", "Is playing changed: $isPlaying")
                callback?.onPlayStateChanged(this@ExoPlayerAdapter)
            }
        })
    }


    private fun notifyBufferStartEnd() {
        callback?.onBufferingStateChanged(
            this@ExoPlayerAdapter, mBufferingStart || !mInitialized
        );
    }

    private fun getUpdatePeriod(): Long {
        return 1000
    }

    override fun setProgressUpdatingEnabled(enabled: Boolean) {
        if (enabled) {
            mHandler.post(mUpdatePositionRunnable)
        } else {
            mHandler.removeCallbacks(mUpdatePositionRunnable)
        }
    }


    override fun onAttachedToHost(host: PlaybackGlueHost) {
        if (host is SurfaceHolderGlueHost) {
            mSurfaceHolderGlueHost = host
            mSurfaceHolderGlueHost!!.setSurfaceHolderCallback(VideoPlayerSurfaceHolderCallback())
        }
        super.onAttachedToHost(host)
    }


    override fun onDetachedFromHost() {
        super.onDetachedFromHost()
        exoPlayer.release()
    }

    override fun isPlaying(): Boolean {
        val exoPlayerIsPlaying: Boolean =
            exoPlayer.playbackState == ExoPlayer.STATE_READY && exoPlayer.playWhenReady
        return mInitialized && exoPlayerIsPlaying
    }

    override fun play() {
        Log.d("ExoPlayerAdapter", "Play called")
        if (!mInitialized || isPlaying()) {
            return
        }
        exoPlayer.playWhenReady = true
        callback?.onPlayStateChanged(this@ExoPlayerAdapter)
        callback?.onCurrentPositionChanged(this@ExoPlayerAdapter)
    }

    override fun pause() {
        Log.d("ExoPlayerAdapter", "Pause called")
        if (isPlaying()) {
            exoPlayer.playWhenReady = false
            callback?.onPlayStateChanged(this@ExoPlayerAdapter)
        }

    }

    override fun seekTo(positionMs: Long) {
        if (!mInitialized) {
            return
        }
        exoPlayer.seekTo(positionMs)
    }

    override fun getBufferedPosition(): Long {
        return exoPlayer.bufferedPosition
    }

    override fun getCurrentPosition(): Long {
        return if (mInitialized) exoPlayer.currentPosition else -1

    }

    override fun getDuration(): Long {
        return if (mInitialized) exoPlayer.duration else -1
    }

    override fun isPrepared(): Boolean {
        return mInitialized && (mSurfaceHolderGlueHost == null || mHasDisplay)
    }

    fun setDataSource(uri: Uri) {
        val mediaItem = MediaItem.fromUri(uri)
        exoPlayer.setMediaItem(mediaItem)
        exoPlayer.prepare()

    }

    internal inner class VideoPlayerSurfaceHolderCallback : SurfaceHolder.Callback {
        override fun surfaceCreated(surfaceHolder: SurfaceHolder) {
            setDisplay(surfaceHolder)
        }

        override fun surfaceChanged(surfaceHolder: SurfaceHolder, i: Int, i1: Int, i2: Int) {}
        override fun surfaceDestroyed(surfaceHolder: SurfaceHolder) {
            setDisplay(null)
        }
    }

    fun setDisplay(surfaceHolder: SurfaceHolder?) {
        val hadDisplay = mHasDisplay
        mHasDisplay = surfaceHolder != null
        if (hadDisplay == mHasDisplay) {
            return
        }
        exoPlayer.setVideoSurfaceHolder(surfaceHolder)
        if (mHasDisplay) {
            if (mInitialized) {
                callback?.onPreparedStateChanged(this)
            }
        } else {
            if (mInitialized) {
                callback?.onPreparedStateChanged(this)
            }
        }
    }
}
