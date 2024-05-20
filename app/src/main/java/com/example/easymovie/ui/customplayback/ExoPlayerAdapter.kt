package com.example.easymovie.ui.customplayback

import android.content.Context
import android.net.Uri
import androidx.leanback.media.PlaybackGlueHost
import androidx.leanback.media.PlayerAdapter
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer

class Media3PlayerAdapter(context: Context) : PlayerAdapter() {

    private val exoPlayer: ExoPlayer = ExoPlayer.Builder(context).build()

    override fun onAttachedToHost(host: PlaybackGlueHost?) {
        super.onAttachedToHost(host)
    }

    override fun onDetachedFromHost() {
        super.onDetachedFromHost()
        exoPlayer.release()
    }

    override fun play() {
        exoPlayer.play()
    }

    override fun pause() {
        exoPlayer.pause()
    }

    override fun seekTo(positionMs: Long) {
        exoPlayer.seekTo(positionMs)
    }

    override fun getCurrentPosition(): Long {
        return exoPlayer.currentPosition
    }

    override fun getDuration(): Long {
        return exoPlayer.duration
    }

    fun setDataSource(uri: Uri) {
        val mediaItem = MediaItem.fromUri(uri)
        exoPlayer.setMediaItem(mediaItem)
        exoPlayer.prepare()
    }

    fun playWhenPrepared() {
        exoPlayer.playWhenReady = true
    }
}
