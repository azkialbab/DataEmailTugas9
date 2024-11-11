package com.example.dataemail.network

import com.example.dataemail.model.Movie
import com.example.dataemail.model.MovieResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {

    @GET("movie")  // Endpoint untuk mengambil daftar film
    fun getMovies(): Call<MovieResponse>  // Menggunakan MovieResponse untuk response yang sesuai

    @POST("movie")
    fun addMovie(@Body movie: Movie): Call<Movie>

    @PUT("movie/{id}")
    fun updateMovie(@Path("id") movieId: Int, @Body movie: Movie): Call<Movie>

    @DELETE("movie/{id}")
    fun deleteMovie(@Path("id") movieId: Int): Call<Void>
}
