package com.example.dataemail

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.dataemail.databinding.FragmentHomeBinding
import com.example.dataemail.model.Movie
import com.example.dataemail.model.MovieResponse
import com.example.dataemail.network.ApiClient
import com.example.dataemail.network.ApiService
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var movieAdapter: MovieAdapter
    private var movieList: MutableList<Movie> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        movieAdapter = MovieAdapter(
            context = requireContext(),
            itemClick = { movie ->
                openMovieDetail(movie)
            },
            addToPlaylist = { movie ->
                addToPlaylist(movie)
            }
        )

        binding.rvMovie.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvMovie.adapter = movieAdapter


        loadMovieData()
    }

    private fun loadMovieData() {
        val client: ApiService = ApiClient.getInstance()
        val response = client.getMovies()

        response.enqueue(object : Callback<MovieResponse> {
            override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    movieList.clear()
                    movieList.addAll(response.body()?.movies ?: emptyList()) // Ambil field `movies`
                    movieAdapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(requireContext(), "Data not found", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                Toast.makeText(requireContext(), "Connection error", Toast.LENGTH_LONG).show()
                Log.e("APIResponse", "Failure: ${t.message}")
            }
        })
    }


    private fun addToPlaylist(movie: Movie) {
        lifecycleScope.launch {
            val dao = AppDatabase.getInstance(requireContext()).movieDao()
            val existingMovie = dao.getMovieById(movie.id)
            if (existingMovie == null) {
                dao.insertMovie(movie)
                Toast.makeText(
                    requireContext(),
                    "${movie.title} added to playlist",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    requireContext(),
                    "${movie.title} is already in the playlist",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun openMovieDetail(movie: Movie) {
        val intent = Intent(requireContext(), DetailActivity::class.java)
        intent.putExtra("MOVIE_TITLE", movie.title)
        intent.putExtra("MOVIE_OVERVIEW", movie.overview)
        intent.putExtra("MOVIE_RELEASE_DATE", movie.releaseDate)
        intent.putExtra("MOVIE_POSTER_PATH", movie.posterPath)
        intent.putExtra("MOVIE_VOTE_AVERAGE", movie.voteAverage)
        intent.putExtra("MOVIE_GENRE", movie.genre)
        intent.putExtra("MOVIE_DURATION", movie.duration)
        intent.putExtra("MOVIE_AGE_RESTRICTION", movie.ageRestriction)
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
