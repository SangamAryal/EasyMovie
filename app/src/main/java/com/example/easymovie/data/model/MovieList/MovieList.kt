package com.example.easymovie.data.model.MovieList

import java.io.Serializable

data class MovieList(
    val page: Int,
    val results: List<Result>,
    val total_pages: Int,
    val total_results: Int
):Serializable