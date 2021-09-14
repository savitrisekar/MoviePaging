package com.example.movie.data.model

import com.google.gson.annotations.SerializedName

data class PopularModel(
    @SerializedName("id")
    val id: Long,

    @SerializedName("title")
    val title: String?,

    @SerializedName("release_date")
    val releaseDate: String?,

    @SerializedName("poster_path")
    val posterPath: String?
)
