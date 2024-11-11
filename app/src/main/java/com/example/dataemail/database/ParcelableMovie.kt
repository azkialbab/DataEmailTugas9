package com.example.dataemail.database

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ParcelableMovie(
    val id: Int,
    val title: String,
    val overview: String,
    val releaseDate: String,
    val posterPath: String,
    val voteAverage: Double,
    val genre: String,
    val duration: Int,
    val ageRestriction: String
) : Parcelable
