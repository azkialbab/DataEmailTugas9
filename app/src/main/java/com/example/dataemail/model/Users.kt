package com.example.dataemail.model

import com.google.gson.annotations.SerializedName

data class Users(
    @SerializedName("data")
    val `data`: List<DataNew>
)
