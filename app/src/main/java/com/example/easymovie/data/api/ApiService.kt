package com.example.easymovie.data.api

import com.example.easymovie.data.model.movielist.MovieList
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("discover/movie")
    suspend fun getMovieList(
        @Query("api_key") apiKey: String
    ): Response<MovieList>

//    @GET("movie/{movie_id}")
//    suspend fun getMovieDetails(
//        @Path("movie_id") movieId: Int,@Query("api_key") apiKey: String):Response<DetailResponse>


}