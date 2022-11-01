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
import retrofit2.Call
import retrofit2.Response

class MoviesActivity : AppCompatActivity() {
    private val moviesApi by lazy { MoviesApiService.create() }
    private val adapter by lazy { MoviesAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movies)
        initUI()

        moviesApi.getPopular(Constants.MOVIESDB_API_KEY)
            .enqueue(object : retrofit2.Callback<MoviesResponse> {
                override fun onResponse(
                    call: Call<MoviesResponse>,
                    response: Response<MoviesResponse>
                ) {
                    Toast.makeText(
                        this@MoviesActivity,
                        "isSuccessful = ${response.isSuccessful}",
                        Toast.LENGTH_SHORT
                    ).show()
                    val moviesResponse = response.body()
                    print(moviesResponse)
                    moviesResponse?.results?.let { populateMoviesList(it) }
                }

                override fun onFailure(call: Call<MoviesResponse>, t: Throwable) {
                    t.printStackTrace()
                }

            })
    }

    private fun initUI() {
        val list = findViewById<RecyclerView>(R.id.moviesList)
        list.adapter = adapter
        list.layoutManager = LinearLayoutManager(this)
    }

    private fun populateMoviesList(movies: List<Movie>) {
        adapter.data = movies
    }
}