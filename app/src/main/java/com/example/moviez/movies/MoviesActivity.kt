package com.example.moviez.movies

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviez.Constants
import com.example.moviez.R
import com.example.moviez.data.MoviesApiService
import com.example.moviez.data.MoviesResponse
import com.example.moviez.models.Movie
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Response
import java.io.IOException
import kotlin.coroutines.CoroutineContext

class MoviesActivity : AppCompatActivity(), CoroutineScope {
    private val moviesApi by lazy { MoviesApiService.create() }
    private val adapter by lazy { MoviesAdapter() }
    private val parentJob = SupervisorJob()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movies)
        initUI()
        fetchData()
    }

    private fun initUI() {
        val list = findViewById<RecyclerView>(R.id.moviesList)
        list.adapter = adapter
        list.layoutManager = LinearLayoutManager(this)
    }

    private fun fetchData() {
        launch {
            val response = getMovies()
            if (response == null) {
                Toast.makeText(this@MoviesActivity, "Unable to get data", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this@MoviesActivity, "Get data successful", Toast.LENGTH_SHORT).show()
                adapter.data = response
            }
        }
    }

    private suspend fun getMovies() = withContext(Dispatchers.IO) {
        try {
            val response = moviesApi.getPopular(Constants.MOVIESDB_API_KEY).execute()
            response.body()?.results
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            fetchData()
        }
    }

    override fun onStop() {
        super.onStop()
        parentJob.cancelChildren()
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + parentJob
}