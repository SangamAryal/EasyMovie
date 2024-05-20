package com.example.easymovie.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.easymovie.data.api.Response
import com.example.easymovie.data.model.MovieList.MovieList
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


}