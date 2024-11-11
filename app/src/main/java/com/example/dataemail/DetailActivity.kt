package com.example.dataemail

import android.content.res.ColorStateList
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.dataemail.R
import com.squareup.picasso.Picasso

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        // Inisialisasi UI komponen
        val ivMoviePoster: ImageView = findViewById(R.id.moviePosterDetail)
        val tvMovieTitle: TextView = findViewById(R.id.movieTitleDetail)
        val tvMovieOverview: TextView = findViewById(R.id.movieSynopsis)
        val tvMovieReleaseDate: TextView = findViewById(R.id.movieReleaseDate)
        val tvMovieVoteAverage: TextView = findViewById(R.id.movieRatingText)
        val tvMovieDuration: TextView = findViewById(R.id.movieDurationGenre) // ID di XML
        val tvMovieAgeRestriction: TextView = findViewById(R.id.movieAgeRestriction)
        val movieRatingBar: RatingBar = findViewById(R.id.movieRating)

        // Toolbar Back Button
        val backButton: ImageButton = findViewById(R.id.backButton)
        backButton.setOnClickListener { finish() }

        // Ambil data dari intent
        val movieTitle = intent.getStringExtra("MOVIE_TITLE")
        val movieOverview = intent.getStringExtra("MOVIE_OVERVIEW")
        val movieReleaseDate = intent.getStringExtra("MOVIE_RELEASE_DATE")
        val moviePosterPath = intent.getStringExtra("MOVIE_POSTER_PATH")
        val movieVoteAverage = intent.getDoubleExtra("MOVIE_VOTE_AVERAGE", 0.0)
        val movieGenre = intent.getStringExtra("MOVIE_GENRE")
        val movieDuration = intent.getIntExtra("MOVIE_DURATION", 0)
        val movieAgeRestriction = intent.getStringExtra("MOVIE_AGE_RESTRICTION")

        // Format teks genre dan durasi
        val formattedDurationGenre = "Duration: $movieDuration minutes | Genre: $movieGenre"

        // Set data ke UI
        tvMovieTitle.text = movieTitle
        tvMovieOverview.text = movieOverview
        tvMovieReleaseDate.text = movieReleaseDate
        tvMovieVoteAverage.text = "Rating: ${movieVoteAverage.toString()}"
        tvMovieDuration.text = formattedDurationGenre
        tvMovieAgeRestriction.text = "Age Restriction: $movieAgeRestriction"

        movieRatingBar.rating = (movieVoteAverage / 2).toFloat()

        // Menentukan warna berdasarkan rating
        val ratingColor = ContextCompat.getColor(this, R.color.red)

        // Ubah warna RatingBar
        movieRatingBar.progressTintList = ColorStateList.valueOf(ratingColor)
        movieRatingBar.secondaryProgressTintList = ColorStateList.valueOf(ratingColor)
        movieRatingBar.secondaryProgressTintList = ColorStateList.valueOf(ratingColor)


        // Memuat gambar poster
        if (!moviePosterPath.isNullOrEmpty()) {
            Picasso.get().load("$moviePosterPath").into(ivMoviePoster)
        }
    }
}
