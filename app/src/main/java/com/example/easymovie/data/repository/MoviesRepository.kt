package com.example.easymovie.data.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.LiveData
import com.example.easymovie.data.api.ApiService
import com.example.easymovie.data.api.Response
import com.example.easymovie.data.model.MovieList.MovieList
import com.example.easymovie.utils.Constants.API_KEY

class MoviesRepository(private val service: ApiService) {
    private val moviesData= MutableLiveData<Response<MovieList>>()

    val movies: LiveData<Response<MovieList>>
        get() = moviesData

    suspend fun getMovieList() {
        try {
            val result = service.getMovieList(API_KEY)

            if (result.isSuccessful && result.body() != null) {
                Log.d("MoviesRepository", "API call successful: ${result.isSuccessful}")
                moviesData.postValue(Response.Success(result.body()))
            } else {
                Log.e("MoviesRepository", "Error: ${result.errorBody()?.string()}")
                moviesData.postValue(Response.Error(result.errorBody().toString()))
            }
        } catch (e: Exception) {
            Log.e("MoviesRepository", "Exception: ${e.message}")
            moviesData.postValue(Response.Error(e.message.toString()))
        }


    }
}