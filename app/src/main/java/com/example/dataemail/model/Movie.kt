package com.example.dataemail.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "movies")
data class Movie(
    @PrimaryKey
    @SerializedName("id")
    val id: Int,

    @SerializedName("title")
    val title: String,

    @SerializedName("overview")
    val overview: String,

    @SerializedName("release_date")
    val releaseDate: String, // Can convert this to Date format later if needed

    @SerializedName("poster_path")
    val posterPath: String,

    @SerializedName("vote_average")
    val voteAverage: Double,

    @SerializedName("genre")
    val genre: String,

    @SerializedName("duration")
    val duration: Int,

    @SerializedName("age_restriction")
    val ageRestriction: String
) : Parcelable
