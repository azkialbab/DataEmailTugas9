package com.example.dataemail.model

import com.google.gson.annotations.SerializedName

data class DataNew(
    @SerializedName("id")
    val id: Int,
    @SerializedName("email")
    val email: String,
    @SerializedName("first_name")
    val firstName: String,
    @SerializedName("last_name")
    val lastName: String,
    @SerializedName("avatar")
    val avatar: String,
) {
    val fullName: String
        get() = "$firstName $lastName"
}
