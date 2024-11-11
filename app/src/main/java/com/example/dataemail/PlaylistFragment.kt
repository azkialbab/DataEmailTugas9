package com.example.dataemail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dataemail.databinding.PlaylistFragmentBinding
import com.example.dataemail.model.Movie
import kotlinx.coroutines.launch

class PlaylistFragment : Fragment() {

    private var _binding: PlaylistFragmentBinding? = null
    private val binding get() = _binding!!

    private val playlist: MutableList<Movie> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = PlaylistFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvPlaylist.layoutManager = LinearLayoutManager(requireContext())
        val playlistAdapter = PlaylistAdapter(playlist, requireContext(), ::deleteMovie)
        binding.rvPlaylist.adapter = playlistAdapter

        lifecycleScope.launch {
            val dao = AppDatabase.getInstance(requireContext()).movieDao()
            val movies = dao.getAllMovies()
            playlist.clear()
            playlist.addAll(movies)
            playlistAdapter.notifyDataSetChanged()
        }
    }

    // Fungsi untuk menghapus movie dari playlist dan database
    private fun deleteMovie(position: Int) {
        lifecycleScope.launch {
            val dao = AppDatabase.getInstance(requireContext()).movieDao()
            val movieToDelete = playlist[position]
            dao.deleteMovieById(movieToDelete.id)  // Hapus dari database berdasarkan ID

            playlist.removeAt(position)  // Hapus dari playlist
            binding.rvPlaylist.adapter?.notifyItemRemoved(position)

            Toast.makeText(requireContext(), "Movie berhasil dihapus", Toast.LENGTH_SHORT).show()// Update RecyclerView
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

