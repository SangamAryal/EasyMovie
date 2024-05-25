package com.example.easymovie.data.model.MovieList

import android.database.Cursor
import androidx.leanback.database.CursorMapper

/**
 * ResultCursorMapper maps a database Cursor to a Result object.
 */
class MovieCursorMapper : CursorMapper() {

    private var adultIndex: Int = 0
    private var backdropPathIndex: Int = 0
    private var genreIdsIndex: Int = 0
    private var idIndex: Int = 0
    private var originalLanguageIndex: Int = 0
    private var originalTitleIndex: Int = 0
    private var overviewIndex: Int = 0
    private var popularityIndex: Int = 0
    private var posterPathIndex: Int = 0
    private var releaseDateIndex: Int = 0
    private var titleIndex: Int = 0
    private var videoIndex: Int = 0
    private var voteAverageIndex: Int = 0
    private var voteCountIndex: Int = 0

    override fun bindColumns(cursor: Cursor) {
        adultIndex = cursor.getColumnIndex("adult")
        backdropPathIndex = cursor.getColumnIndex("backdrop_path")
        genreIdsIndex = cursor.getColumnIndex("genre_ids")
        idIndex = cursor.getColumnIndex("id")
        originalLanguageIndex = cursor.getColumnIndex("original_language")
        originalTitleIndex = cursor.getColumnIndex("original_title")
        overviewIndex = cursor.getColumnIndex("overview")
        popularityIndex = cursor.getColumnIndex("popularity")
        posterPathIndex = cursor.getColumnIndex("poster_path")
        releaseDateIndex = cursor.getColumnIndex("release_date")
        titleIndex = cursor.getColumnIndex("title")
        videoIndex = cursor.getColumnIndex("video")
        voteAverageIndex = cursor.getColumnIndex("vote_average")
        voteCountIndex = cursor.getColumnIndex("vote_count")
    }

    override fun bind(cursor: Cursor): Any {

        val adult = cursor.getInt(adultIndex) == 1
        val backdropPath = cursor.getString(backdropPathIndex)
        val genreIds = cursor.getString(genreIdsIndex).split(",").map { it.toInt() }
        val id = cursor.getInt(idIndex)
        val originalLanguage = cursor.getString(originalLanguageIndex)
        val originalTitle = cursor.getString(originalTitleIndex)
        val overview = cursor.getString(overviewIndex)
        val popularity = cursor.getDouble(popularityIndex)
        val posterPath = cursor.getString(posterPathIndex)
        val releaseDate = cursor.getString(releaseDateIndex)
        val title = cursor.getString(titleIndex)
        val video = cursor.getInt(videoIndex) == 1
        val voteAverage = cursor.getDouble(voteAverageIndex)
        val voteCount = cursor.getInt(voteCountIndex)

        // Build a Result object to be processed.
        return Result(
            adult,
            backdropPath,
            genreIds,
            id,
            originalLanguage,
            originalTitle,
            overview,
            popularity,
            posterPath,
            releaseDate,
            title,
            video,
            voteAverage,
            voteCount
        )
    }
}
