package com.example.moviez.data

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface MoviesApiService {
    @GET("/3/movie/popular")
    fun getPopular(@Query("api_key") apiKey: String): Call<MoviesResponse>

    companion object {
        fun create(): MoviesApiService {
            val retrofit = Retrofit.Builder()
                .baseUrl("https://api.themoviedb.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit.create(MoviesApiService::class.java)
        }
    }
}