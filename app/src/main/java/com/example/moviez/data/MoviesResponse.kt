package com.example.moviez.data

import com.example.moviez.models.Movie

data class MoviesResponse(
    val page: Int,
    val results: List<Movie>,
    val total_results: Int,
    val total_pages: Int
)