package com.example.easymovie.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.easymovie.data.api.ApiService
import com.example.easymovie.data.api.Response
import com.example.easymovie.data.model.movielist.MovieList
import com.example.easymovie.data.model.movielist.Result
import com.example.easymovie.utils.Constants.API_KEY
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MoviesRepository(private val service: ApiService) {

    private val moviesData = MutableLiveData<Response<MovieList>>()
    private val searchData = MutableLiveData<List<Result>>()

    val movies: LiveData<Response<MovieList>>
        get() = moviesData

    val searchResults: LiveData<List<Result>>
        get() = searchData

    suspend fun getMovieList() {
        withContext(Dispatchers.IO) {
            try {
                val result = service.getMovieList(API_KEY)
                if (result.isSuccessful && result.body() != null) {
                    Log.d("MoviesRepository", "API call successful: ${result.isSuccessful}")
                    moviesData.postValue(Response.Success(result.body()))
                }
                else {
                    Log.e("MoviesRepository", "Error: ${result.errorBody()?.string()}")
                    moviesData.postValue(Response.Error(result.errorBody().toString()))
                }
            } catch (e: Exception) {
                Log.e("MoviesRepository", "Exception: ${e.message}")
                moviesData.postValue(Response.Error(e.message.toString()))
            }
        }
    }

    fun searchMovies(query: String) {
        val currentMovies = (moviesData.value as? Response.Success)?.data?.results ?: emptyList()
        searchData.postValue(currentMovies.filter {
            it.title.contains(query, ignoreCase = true) ||
                    it.overview.contains(query, ignoreCase = true)
        })
    }
}
