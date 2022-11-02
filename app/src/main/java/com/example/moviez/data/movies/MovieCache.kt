package com.example.moviez.data.movies

import com.example.moviez.data.AppDatabase
import com.example.moviez.models.Movie

class MovieCache(private val db: AppDatabase) {
    private val movieDao: MovieDao by lazy { db.movieDao() }

    fun setCache(movies: List<Movie>) {
        movieDao.deleteAll()
        movieDao.insertAll(movies)
    }

    fun getCache(): List<Movie> {
        return movieDao.getAll()
    }
}