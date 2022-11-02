package com.example.moviez.movies

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviez.R

class MoviesActivity : AppCompatActivity() {
    private val viewModel: MoviesViewModel by viewModels()
    private val adapter by lazy { MoviesAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movies)
        initUI()
        initViewModel()
    }

    private fun initUI() {
        val list = findViewById<RecyclerView>(R.id.moviesList)
        list.adapter = adapter
        list.layoutManager = LinearLayoutManager(this)
    }

    private fun initViewModel() {
        viewModel.movies.observe(this) {
            adapter.data = it
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            viewModel.fetchMovies()
        }
    }
}