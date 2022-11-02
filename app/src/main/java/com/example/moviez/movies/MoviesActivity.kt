package com.example.moviez.movies

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviez.R
import com.google.android.material.progressindicator.LinearProgressIndicator

class MoviesActivity : AppCompatActivity() {
    private val viewModel: MoviesViewModel by viewModels { MoviesViewModel.Factory }
    private val adapter by lazy { MoviesAdapter() }
    private lateinit var progressBar: LinearProgressIndicator

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
        progressBar = findViewById(R.id.progressBar)
    }

    private fun initViewModel() {
        viewModel.movies.observe(this) {
            adapter.data = it
        }
        viewModel.isCached.observe(this) {
            val appName = getString(R.string.app_name)
            this.title = appName + if (it) " (cached)" else ""
        }
        viewModel.isFetching.observe(this) {
            progressBar.visibility = if (it) View.VISIBLE else View.GONE

        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            viewModel.fetchMovies()
        }
    }
}