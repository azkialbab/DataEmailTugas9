package com.example.dataemail

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.dataemail.databinding.ItemMovieBinding
import com.example.dataemail.model.Movie
import com.squareup.picasso.Picasso

class MovieAdapter(
    private val context: Context,
    private val itemClick: (Movie) -> Unit,
    private val addToPlaylist: (Movie) -> Unit
) : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    private val movieList: MutableList<Movie> = mutableListOf()

    inner class MovieViewHolder(private val binding: ItemMovieBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(movie: Movie) {
            binding.movieTitle.text = movie.title
            binding.movieDuration.text = "${movie.duration} minutes"
            binding.movieAge.text = movie.ageRestriction
            binding.movieRatingBar.rating = (movie.voteAverage / 2).toFloat()

            val posterUrl = "${movie.posterPath}"
            println(posterUrl)
            Picasso.get().load(posterUrl).into(binding.moviePoster)


            binding.moviePoster.setOnClickListener {
                itemClick(movie)
            }


            binding.addPlaylist.setOnClickListener {
                addToPlaylist(movie)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = ItemMovieBinding.inflate(LayoutInflater.from(context), parent, false)
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = movieList[position]
        holder.bind(movie)
    }

    override fun getItemCount(): Int = movieList.size

    fun setMovies(movies: List<Movie>) {
        movieList.clear()
        movieList.addAll(movies)
        notifyDataSetChanged()
    }
}
