package com.example.dataemail.admin

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.dataemail.databinding.ActivityDetailMovieBinding
import com.example.dataemail.model.Movie
import com.example.dataemail.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailMovie : AppCompatActivity() {

    private lateinit var binding: ActivityDetailMovieBinding
    private lateinit var movie: Movie // Model data untuk film

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailMovieBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Mendapatkan data film dari intent
        movie = intent.getParcelableExtra("movie") ?: return

        // Menampilkan data film ke UI
        displayMovieDetails()

        // Tombol back
        binding.btnBack.setOnClickListener {
            onBackPressed()
        }

        // Tombol Edit
        binding.btnEdit.setOnClickListener {
            val intent = Intent(this, UpdateMovie::class.java)
            intent.putExtra("movie", movie)
            startActivityForResult(intent, MovieCollection.UPDATE_MOVIE_REQUEST_CODE)
        }

        // Tombol Delete
        binding.btnDelete.setOnClickListener {
            deleteMovie()
        }
    }

    private fun displayMovieDetails() {
        Glide.with(this)
            .load(movie.posterPath)
            .into(binding.moviePosterDetail)

        binding.movieTitleDetail.text = movie.title
        binding.movieDurationGenreDetail.text = "Durasi: ${movie.duration} | Genre: ${movie.genre}"
        binding.movieSynopsis.text = "Sinopsis: ${movie.overview}"
        binding.movieRating.text = "Rating: ${movie.voteAverage}"
        binding.movieAgeRestrictionDetail.text = "Age Restriction: ${movie.ageRestriction}"
    }

    private fun deleteMovie() {
        AlertDialog.Builder(this)
            .setTitle("Hapus Film")
            .setMessage("Apakah Anda yakin ingin menghapus film ini?")
            .setPositiveButton("Ya") { _, _ ->
                deleteMovieFromDatabase()
            }
            .setNegativeButton("Tidak", null)
            .create()
            .show()
    }

    private fun deleteMovieFromDatabase() {
        val apiService = ApiClient.getInstance()
        val movieId = movie.id

        apiService.deleteMovie(movieId).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@DetailMovie, "Film berhasil dihapus", Toast.LENGTH_SHORT).show()
                    setResult(RESULT_OK) // Mengirimkan hasil ke MovieCollection
                    finish()
                } else {
                    Toast.makeText(this@DetailMovie, "Gagal menghapus film", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(this@DetailMovie, "Terjadi kesalahan: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}