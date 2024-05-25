package com.example.easymovie.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.easymovie.data.api.Response
import com.example.easymovie.data.model.MovieList.MovieList
import com.example.easymovie.data.model.MovieList.Result
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

    private val _searchResults = MutableLiveData<List<Result>>()
    val searchResults: LiveData<List<Result>>
        get() = _searchResults

    fun searchMovies(query: String) {
        val currentMovies = (moviesList.value as? Response.Success)?.data?.results ?: emptyList()
        _searchResults.postValue(currentMovies.filter {
            it.title.contains(query, ignoreCase = true) ||
                    it.overview.contains(query, ignoreCase = true)
        })
    }
}
