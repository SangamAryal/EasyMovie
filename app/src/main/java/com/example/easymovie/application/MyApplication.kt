package com.example.easymovie.application

import android.app.Application
import com.example.easymovie.data.api.ApiService
import com.example.easymovie.data.api.RetrofitHelper
import com.example.easymovie.data.repository.MoviesRepository

class MyApplication : Application() {
    lateinit var moviesRepository: MoviesRepository
    override fun onCreate() {
        super.onCreate()
        init()
    }

    private fun init() {
        val service = RetrofitHelper.getInstance().create(ApiService::class.java)
        moviesRepository = MoviesRepository(service)
    }
}