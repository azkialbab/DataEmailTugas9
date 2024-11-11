package com.example.dataemail.admin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dataemail.databinding.ActivityMovieCollectionBinding
import com.example.dataemail.model.Movie
import com.example.dataemail.model.MovieResponse
import com.example.dataemail.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieCollection : AppCompatActivity() {

    private lateinit var binding: ActivityMovieCollectionBinding
    private lateinit var collectionAdapter: CollectionAdapter
    private var movieList = mutableListOf<Movie>()

    companion object {
        const val ADD_MOVIE_REQUEST_CODE = 1
        const val UPDATE_MOVIE_REQUEST_CODE = 2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieCollectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        fetchMovies()

        binding.addMovieText.setOnClickListener {
            val intent = Intent(this, AddMovie::class.java)
            startActivityForResult(intent, ADD_MOVIE_REQUEST_CODE)
        }
    }

    private fun setupRecyclerView() {
        collectionAdapter = CollectionAdapter(
            movieList,
            onViewClick = { movie -> viewMovieDetails(movie) },
            onEditClick = { movie -> editMovie(movie) },
            onDeleteClick = { movie -> deleteMovie(movie) }
        )

        binding.rvMovieCollection.apply {
            layoutManager = LinearLayoutManager(this@MovieCollection)
            adapter = collectionAdapter
        }
    }

    private fun fetchMovies() {
        val apiService = ApiClient.getInstance()
        apiService.getMovies().enqueue(object : Callback<MovieResponse> {  // Ubah ke MovieResponse
            override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        movieList.clear()
                        movieList.addAll(it.movies)  // Akses daftar film dari MovieResponse
                        collectionAdapter.notifyDataSetChanged()
                    }
                } else {
                    Log.e("ApiClient", "Response Error: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                Log.e("ApiClient", "Request Failed: ${t.message}")
            }
        })
    }


    private fun deleteMovie(movie: Movie) {
        val movieId = movie.id  // Gunakan ID sebagai Int, tanpa mengubahnya ke String
        if (movieId == 0) {
            Log.e("ApiClient", "Invalid movie ID: $movieId")
            return
        }

        val apiService = ApiClient.getInstance()
        apiService.deleteMovie(movieId).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {

                    val position = movieList.indexOf(movie)
                    if (position >= 0) {
                        movieList.removeAt(position)
                        collectionAdapter.notifyItemRemoved(position)
                    }
                } else {
                    Log.e("ApiClient", "Error deleting movie: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("ApiClient", "Failed to delete movie: ${t.message}")
            }
        })
    }


    private fun viewMovieDetails(movie: Movie) {
        val intent = Intent(this, DetailMovie::class.java)
        intent.putExtra("movie", movie)  // Menambahkan objek Movie ke Intent
        startActivityForResult(intent, UPDATE_MOVIE_REQUEST_CODE)
    }

    private fun editMovie(movie: Movie) {
        val intent = Intent(this, UpdateMovie::class.java)
        intent.putExtra("movie", movie)  // Kirim data movie ke UpdateMovie
        startActivityForResult(intent, UPDATE_MOVIE_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == ADD_MOVIE_REQUEST_CODE && resultCode == RESULT_OK) {
            // Menangani hasil dari AddMovie
            val newMovie = data?.getParcelableExtra<Movie>("movie")
            newMovie?.let {
                movieList.add(it)  // Menambahkan film baru ke daftar
                collectionAdapter.notifyItemInserted(movieList.size - 1) // Memberitahukan adapter
            }
        } else if (requestCode == UPDATE_MOVIE_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            val updatedMovie = data.getParcelableExtra<Movie>("updatedMovie")
            updatedMovie?.let {
                updateMovieInList(it)  // Memperbarui data di movieList
            }
        }
    }

    private fun updateMovieInList(updatedMovie: Movie) {
        val position = movieList.indexOfFirst { it.id == updatedMovie.id }
        if (position != -1) {
            movieList[position] = updatedMovie
            collectionAdapter.notifyItemChanged(position)
        }
    }
}
