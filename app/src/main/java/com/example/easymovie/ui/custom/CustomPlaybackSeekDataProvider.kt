package com.example.easymovie.ui.custom

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.util.Log
import android.util.SparseArray
import androidx.leanback.widget.PlaybackSeekDataProvider
import kotlinx.coroutines.*
import java.io.File
import java.io.FileOutputStream

class CustomPlaybackSeekDataProvider(
    private val videoUrl: String,
    private val context: Context,
    private val intervalInSeconds: Int
) : PlaybackSeekDataProvider() {

    private var seekPositions: LongArray = LongArray(0)
    private val thumbnails: SparseArray<Bitmap> = SparseArray()
    private var thumbnailDirectoryPath: String = context.filesDir.absolutePath + "/thumbnails"
    private val scope = CoroutineScope(Dispatchers.Main)

    init {
        val dir = File(thumbnailDirectoryPath)
        if (!dir.exists()) {
            dir.mkdirs()
        }

        scope.launch {
            generateThumbnails(videoUrl, thumbnailDirectoryPath, intervalInSeconds)
            loadThumbnails(videoUrl)
        }
    }

    private suspend fun generateThumbnails(
        videoUrl: String,
        thumbnailDirectoryPath: String,
        intervalInSeconds: Int
    ) = withContext(Dispatchers.IO) {
        val metadataRetriever = MediaMetadataRetriever()
        try {
            metadataRetriever.setDataSource(videoUrl, hashMapOf())
            val videoLength =
                metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
                    ?.toLong() ?: 0L
            val thumbnailCount = (videoLength / 1000 / intervalInSeconds).toInt()

            for (i in 0 until thumbnailCount) {
                val frameTime = i * intervalInSeconds * 1000 * 1000L // Convert to microseconds
                val bitmap = metadataRetriever.getFrameAtTime(
                    frameTime,
                    MediaMetadataRetriever.OPTION_CLOSEST_SYNC
                )
                if (bitmap != null) {
                    saveThumbnail(bitmap, thumbnailDirectoryPath, i)
                } else {
                    Log.e(TAG, "Failed to retrieve frame at index $i, time: $frameTime")
                }
            }
        } catch (e: IllegalArgumentException) {
            Log.e(TAG, "Error generating thumbnails: Invalid video URL or parameters: $e")
        } catch (e: Exception) {
            Log.e(TAG, "Error generating thumbnails: $e")
        } finally {
            metadataRetriever.release()
        }
    }

    private suspend fun loadThumbnails(videoUrl: String) = withContext(Dispatchers.IO) {
        val metadataRetriever = MediaMetadataRetriever()
        try {
            metadataRetriever.setDataSource(videoUrl, hashMapOf())
            val videoLength =
                metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
                    ?.toLong() ?: 0L
            val interval = intervalInSeconds * 1000L // Convert to milliseconds
            val thumbnailCount = (videoLength / interval).toInt()
            val positions = LongArray(thumbnailCount) { it * interval }
            for (i in positions.indices) {
                val thumbnailPath = "$thumbnailDirectoryPath/thumb${String.format("%04d", i)}.jpg"
                val bitmap = BitmapFactory.decodeFile(thumbnailPath)
                if (bitmap != null) {
                    thumbnails.put(i, bitmap)
                } else {
                    Log.e(TAG, "Unable to decode thumbnail at index $i, path: $thumbnailPath")
                }
            }
            seekPositions = positions
        } catch (e: Exception) {
            Log.e(TAG, "Error during metadata retrieval or thumbnail loading: $e")
        } finally {
            metadataRetriever.release()
        }
    }

    private fun saveThumbnail(bitmap: Bitmap, thumbnailDirectoryPath: String, index: Int) {
        val fileName = "thumb${String.format("%04d", index)}.jpg"
        val outputFile = File(thumbnailDirectoryPath, fileName)
        try {
            FileOutputStream(outputFile).use { out ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
                Log.d(
                    TAG,
                    "Saved thumbnail at index $index, path: $thumbnailDirectoryPath/$fileName"
                )
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error saving thumbnail at index $index: $e")
        }
    }

    override fun getSeekPositions(): LongArray {
        return seekPositions
    }

    override fun getThumbnail(index: Int, callback: PlaybackSeekDataProvider.ResultCallback?) {
        callback?.onThumbnailLoaded(thumbnails.get(index), index)
    }

    companion object {
        private const val TAG = "CustomPlaybackSeekData"
    }
}
