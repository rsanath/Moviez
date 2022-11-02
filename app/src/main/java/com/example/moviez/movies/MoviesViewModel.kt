package com.example.moviez.movies

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviez.Constants
import com.example.moviez.data.AppDatabase
import com.example.moviez.data.movies.MovieCache
import com.example.moviez.data.movies.MoviesApiService
import com.example.moviez.models.Movie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MoviesViewModel : ViewModel() {
    lateinit var db: AppDatabase // todo use dependency injection
    private val movieCache: MovieCache by lazy { MovieCache(db) }

    val movies = MutableLiveData<List<Movie>>(listOf())
    val isCached = MutableLiveData<Boolean>(false)

    private val moviesApi by lazy { MoviesApiService.create() }

    fun fetchMovies() {
        viewModelScope.launch {
            val moviesList = getMovies()
            moviesList?.let { movies.value = it }
        }
    }

    private suspend fun getMovies() = withContext(Dispatchers.IO) {
        val cachedMovies = movieCache.getCache()
        try {
            val response = moviesApi.getPopular(Constants.MOVIESDB_API_KEY)
            val movies = response.results
            movieCache.setCache(movies)
            setCached(false)
            movies
        } catch (e: Exception) {
            e.printStackTrace()
            setCached(true)
            cachedMovies.ifEmpty { null }
        }
    }

    private suspend fun setCached(flag: Boolean) = withContext(Dispatchers.Main) {
        isCached.value = flag
    }

    companion object {
        const val TAG = "MoviesViewModel"
    }
}