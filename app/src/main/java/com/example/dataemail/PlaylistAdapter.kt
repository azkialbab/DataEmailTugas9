package com.example.dataemail

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.dataemail.databinding.ItemMovieBinding
import com.example.dataemail.databinding.ItemPlaylistBinding
import com.example.dataemail.model.Movie
import com.squareup.picasso.Picasso

class PlaylistAdapter(
    private val playlist: MutableList<Movie>,
    private val context: Context,
    private val deleteMovie: (Int) -> Unit
) : RecyclerView.Adapter<PlaylistAdapter.PlaylistViewHolder>() {
    inner class PlaylistViewHolder(private val binding: ItemPlaylistBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(movie: Movie) {
            binding.movieTitle.text = movie.title
            binding.movieDuration.text = "${movie.duration} minutes"
            binding.movieAge.text = movie.ageRestriction
            binding.movieRatingBar.rating = (movie.voteAverage / 2).toFloat()

            val posterUrl = "${movie.posterPath}"
            Picasso.get().load(posterUrl).into(binding.moviePoster)


            binding.deleteIcon.setOnClickListener {
                deleteMovie(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val binding = ItemPlaylistBinding.inflate(LayoutInflater.from(context), parent, false)
        return PlaylistViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        val movie = playlist[position]
        holder.bind(movie)
    }

    override fun getItemCount(): Int {
        return playlist.size
    }
}

