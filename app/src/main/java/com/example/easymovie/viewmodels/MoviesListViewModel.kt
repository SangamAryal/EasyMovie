package com.example.easymovie.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.easymovie.data.api.Response
import com.example.easymovie.data.model.movielist.MovieList
import com.example.easymovie.data.model.movielist.Result
import com.example.easymovie.data.repository.MoviesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MoviesListViewModel(private val moviesRepository: MoviesRepository) : ViewModel() {

    init {
        viewModelScope.launch(Dispatchers.IO) {
            moviesRepository.getMovieList()
        }
    }

    val moviesList: LiveData<Response<MovieList>>
        get() = moviesRepository.movies
    val searchResults: LiveData<List<Result>>
        get() = moviesRepository.searchResults


    fun searchMovies(query: String) {
        viewModelScope.launch {
            moviesRepository.searchMovies(query)
        }
    }
}
