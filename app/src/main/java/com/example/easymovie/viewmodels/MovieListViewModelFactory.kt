package com.example.easymovie.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.easymovie.data.repository.MoviesRepository

class MovieListViewModelFactory(private val moviesRepository: MoviesRepository):ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MoviesListViewModel(moviesRepository) as T
    }

}