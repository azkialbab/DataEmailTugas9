package com.example.dataemail.admin

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.dataemail.R
import com.example.dataemail.model.Movie
import com.example.dataemail.network.ApiClient
import com.google.android.material.button.MaterialButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpdateMovie : AppCompatActivity() {

    private lateinit var titleEditText: EditText
    private lateinit var genreEditText: EditText
    private lateinit var durationEditText: EditText
    private lateinit var releaseDateEditText: EditText
    private lateinit var ageRestrictionEditText: EditText
    private lateinit var overviewEditText: EditText
    private lateinit var movie: Movie
    private lateinit var updateButton: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_movie)

        // Initialize EditText views
        titleEditText = findViewById(R.id.movieName)
        genreEditText = findViewById(R.id.movieGenre)
        durationEditText = findViewById(R.id.movieDuration)
        releaseDateEditText = findViewById(R.id.etReleaseDate)
        ageRestrictionEditText = findViewById(R.id.etAgeRestriction)
        overviewEditText = findViewById(R.id.oMovie)

        // Receive the movie object passed from MovieCollection activity
        movie = intent.getParcelableExtra("movie")!!

        // Pre-fill data in EditText fields
        titleEditText.setText(movie.title)
        genreEditText.setText(movie.genre)
        durationEditText.setText(movie.duration.toString())
        releaseDateEditText.setText(movie.releaseDate)
        ageRestrictionEditText.setText(movie.ageRestriction)
        overviewEditText.setText(movie.overview)

        // Initialize Update button
        updateButton = findViewById(R.id.btnUpdateMovie)

        // Set the click listener for the update button
        updateButton.setOnClickListener {
            // Validate and update movie details
            if (validateInputs()) {
                updateMovie(movie.id)  // Pass the movie ID for updating
            } else {
                Toast.makeText(this, "Please fill in all fields correctly", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Validate input fields to ensure no field is empty or invalid
    private fun validateInputs(): Boolean {
        return titleEditText.text.isNotEmpty() &&
                genreEditText.text.isNotEmpty() &&
                durationEditText.text.toString().toIntOrNull() != null &&
                releaseDateEditText.text.isNotEmpty() &&
                ageRestrictionEditText.text.isNotEmpty() &&
                overviewEditText.text.isNotEmpty()
    }

    // Update movie details via API call
    private fun updateMovie(movieId: Int) {
        val updatedMovie = Movie(
            id = movieId, // Use the existing ID for updating
            title = titleEditText.text.toString(),
            genre = genreEditText.text.toString(),
            duration = durationEditText.text.toString().toInt(),
            releaseDate = releaseDateEditText.text.toString(),
            ageRestriction = ageRestrictionEditText.text.toString(),
            voteAverage = movie.voteAverage, // Keep the existing vote average
            posterPath = movie.posterPath, // Keep the existing poster
            overview = overviewEditText.text.toString()
        )

        // API client to make the request for updating the movie
        val apiService = ApiClient.getInstance()
        apiService.updateMovie(movieId, updatedMovie).enqueue(object : Callback<Movie> {
            override fun onResponse(call: Call<Movie>, response: Response<Movie>) {
                if (response.isSuccessful) {
                    // Film berhasil diperbarui
                    Toast.makeText(this@UpdateMovie, "Film berhasil diperbarui", Toast.LENGTH_SHORT).show()

                    // Send updated movie data back to MovieCollection activity
                    val intent = Intent()
                    intent.putExtra("updatedMovie", response.body()) // Send updated movie
                    setResult(RESULT_OK, intent) // Set the result to be returned
                    finish() // Close the activity and return to MovieCollection
                } else {
                    // Handle response error
                    Toast.makeText(this@UpdateMovie, "Gagal memperbarui film: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Movie>, t: Throwable) {
                // Handle network failure
                Toast.makeText(this@UpdateMovie, "Kesalahan jaringan: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
