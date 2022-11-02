package com.example.moviez.movies

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviez.Constants
import com.example.moviez.data.MoviesApiService
import com.example.moviez.models.Movie
import kotlinx.coroutines.*

class MoviesViewModel : ViewModel() {
    val movies = MutableLiveData<List<Movie>>(listOf())

    private val moviesApi by lazy { MoviesApiService.create() }

    fun fetchMovies() {
        viewModelScope.launch {
            val moviesList = getMovies()
            moviesList?.let { movies.value = it }
        }
    }

    private suspend fun getMovies() = withContext(Dispatchers.IO) {
        try {
            val response = moviesApi.getPopular(Constants.MOVIESDB_API_KEY)
            response.results
        } catch (e: Exception) {
            e.printStackTrace() // todo send cache
            null
        }
    }

}