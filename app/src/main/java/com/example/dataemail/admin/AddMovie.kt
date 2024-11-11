package com.example.dataemail.admin

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.dataemail.databinding.ActivityAddMovieBinding
import com.example.dataemail.model.Movie
import com.example.dataemail.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddMovie : AppCompatActivity() {
    private lateinit var binding: ActivityAddMovieBinding
    private val apiService = ApiClient.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddMovieBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Button Back
        binding.btnBack.setOnClickListener {
            onBackPressed()
        }

        // Tombol Save
        binding.btnSaveMovie.setOnClickListener {
            val movie = createMovieFromInput()

            if (movie != null) {
                saveMovie(movie)
            } else {
                Toast.makeText(this, "Please fill in all fields correctly", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun createMovieFromInput(): Movie? {
        val title = binding.movieName.text.toString().trim()
        val genre = binding.movieGenre.text.toString().trim()
        val duration = binding.movieDuration.text.toString().trim().toIntOrNull()
        val ageRestriction = binding.etAgeRestriction.text.toString().trim()
        val releaseDate = binding.etReleaseDate.text.toString().trim()
        val posterLink = binding.etPosterLink.text.toString().trim()
        val overview = binding.etOverview.text.toString().trim()
        val voteAverage = binding.etVoteAverage.text.toString().trim().toDoubleOrNull()

        if (title.isEmpty() || genre.isEmpty() || duration == null || ageRestriction.isEmpty() ||
            releaseDate.isEmpty() || posterLink.isEmpty() || voteAverage == null) {
            return null
        }

        return Movie(
            id = 0, // ID will be set by backend or auto-generated
            title = title,
            overview = overview,
            releaseDate = releaseDate,
            posterPath = posterLink,
            voteAverage = voteAverage,
            genre = genre,
            duration = duration,
            ageRestriction = ageRestriction
        )
    }

    private fun saveMovie(movie: Movie) {
        apiService.addMovie(movie).enqueue(object : Callback<Movie> {
            override fun onResponse(call: Call<Movie>, response: Response<Movie>) {
                if (response.isSuccessful) {
                    val savedMovie = response.body()
                    if (savedMovie != null) {
                        Toast.makeText(this@AddMovie, "Movie Saved!", Toast.LENGTH_SHORT).show()

                        // Returning the saved movie to MovieCollectionActivity
                        val intent = Intent()
                        intent.putExtra("movie", savedMovie)
                        setResult(RESULT_OK, intent)
                        finish() // Close AddMovie and return to MovieCollection
                    }
                } else {
                    Toast.makeText(this@AddMovie, "Failed to save movie", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Movie>, t: Throwable) {
                Toast.makeText(this@AddMovie, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
