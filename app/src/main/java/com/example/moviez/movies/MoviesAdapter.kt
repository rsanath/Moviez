package com.example.moviez.movies

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moviez.R
import com.example.moviez.models.Movie
import com.google.android.material.imageview.ShapeableImageView

class MoviesAdapter : RecyclerView.Adapter<MoviesAdapter.MovieViewHolder>() {
    var data = listOf<Movie>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.item_movie,
            parent,
            false
        )
        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = data[position]
        holder.poster.shapeAppearanceModel = holder.poster.shapeAppearanceModel
            .toBuilder()
            .setAllCornerSizes(10f)
            .build()
        Glide
            .with(holder.poster.context)
            .load("https://image.tmdb.org/t/p/original/${movie.poster_path}")
            .centerCrop()
            .into(holder.poster)
        holder.title.text = movie.title
        holder.description.text = movie.overview
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class MovieViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        var poster: ShapeableImageView
        var title: TextView
        var description: TextView

        init {
            poster = item.findViewById(R.id.posterImage)
            title = item.findViewById(R.id.title)
            description = item.findViewById(R.id.description)
        }
    }
}