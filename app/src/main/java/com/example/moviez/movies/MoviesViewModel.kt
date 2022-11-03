package com.example.moviez.movies

import androidx.lifecycle.*
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.moviez.Constants
import com.example.moviez.data.AppDatabase
import com.example.moviez.data.movies.MovieCache
import com.example.moviez.data.movies.MoviesApiService
import com.example.moviez.models.Movie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MoviesViewModel(db: AppDatabase) : ViewModel() {
    private val movieCache: MovieCache by lazy { MovieCache(db) }

    val movies = MutableLiveData(listOf<Movie>())
    val isFetching = MutableLiveData(false)
    val isCached = MutableLiveData(false)

    private val moviesApi by lazy { MoviesApiService.create() }

    fun fetchMovies() {
        viewModelScope.launch {
            if (movies.value!!.isEmpty()) {
                movies.value = getCachedMovies()
            }
            val moviesList = getMovies()
            moviesList?.let { movies.value = it }
        }
    }

    private suspend fun getCachedMovies() = withContext(Dispatchers.IO) {
        movieCache.getCache()
    }

    private suspend fun getMovies() = withContext(Dispatchers.IO) {
        try {
            setFetching(true)
            val response = moviesApi.getPopular(Constants.MOVIESDB_API_KEY)
            val movies = response.results
            movieCache.setCache(movies)
            setCached(false)
            setFetching(false)
            movies
        } catch (e: Exception) {
            e.printStackTrace()
            setFetching(false)
            null
        }
    }

    private suspend fun setFetching(flag: Boolean) = withContext(Dispatchers.Main) {
        isFetching.value = flag
    }

    private suspend fun setCached(flag: Boolean) = withContext(Dispatchers.Main) {
        isCached.value = flag
    }

    companion object {
        const val TAG = "MoviesViewModel"

        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>, extras: CreationExtras
            ): T {
                val application = checkNotNull(
                    extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]
                )
                val db = AppDatabase.createDb(application)
                return MoviesViewModel(db) as T
            }
        }
    }
}