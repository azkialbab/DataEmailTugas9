package com.example.dataemail.model

import com.google.gson.annotations.SerializedName

data class MovieResponse(
    @SerializedName("movies")
    val movies: List<Movie>
)
