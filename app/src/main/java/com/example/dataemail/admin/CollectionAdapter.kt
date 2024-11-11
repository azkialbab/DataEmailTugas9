package com.example.dataemail.admin

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.dataemail.R
import com.example.dataemail.databinding.ItemCollectionBinding
import com.example.dataemail.model.Movie

class CollectionAdapter(
    private val movieList: MutableList<Movie>,  // Using MutableList for dynamic updates
    private val onViewClick: (Movie) -> Unit,
    private val onEditClick: (Movie) -> Unit,
    private val onDeleteClick: (Movie) -> Unit
) : RecyclerView.Adapter<CollectionAdapter.MovieViewHolder>() {

    inner class MovieViewHolder(private val binding: ItemCollectionBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(movie: Movie) {
            binding.apply {
                movieTitle.text = movie.title
                movieDuration.text = "Durasi: ${movie.duration}m | Genre: ${movie.genre}"

                // Memuat gambar poster dari URL yang sudah ada di movie.posterPath
                if (!movie.posterPath.isNullOrEmpty()) {
                    Glide.with(moviePoster.context)
                        .load(movie.posterPath)  // Langsung menggunakan movie.posterPath yang berisi URL gambar
                        .override(300, 300)
                        .centerCrop()
                        .placeholder(R.drawable.sample)  // Placeholder jika gambar belum dimuat
                        .error(R.drawable.logo)  // Gambar error jika tidak ditemukan
                        .diskCacheStrategy(DiskCacheStrategy.ALL)  // Menyimpan gambar di cache
                        .into(moviePoster)
                } else {
                    Glide.with(moviePoster.context)
                        .load(R.drawable.logo)  // Gambar default jika posterPath kosong
                        .into(moviePoster)
                }

                // Menangani klik untuk melihat detail film
                btnView.setOnClickListener { onViewClick(movie) }

                // Menangani klik untuk mengedit film
                btnEdit.setOnClickListener { onEditClick(movie) }

                // Menangani klik untuk menghapus film dengan konfirmasi
                btnDelete.setOnClickListener {
                    AlertDialog.Builder(binding.root.context)
                        .setTitle("Hapus Film")
                        .setMessage("Apakah Anda yakin ingin menghapus film ini?")
                        .setPositiveButton("Ya") { _, _ ->
                            onDeleteClick(movie)  // Menjalankan fungsi penghapusan
                        }
                        .setNegativeButton("Tidak", null)
                        .create()
                        .show()
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = ItemCollectionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = movieList[position]
        holder.bind(movie)
    }

    override fun getItemCount(): Int = movieList.size
}
