package com.example.movieapp.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * ジャンル情報
 */
data class Genre(
    @SerializedName("id")
    val id: Int,

    @SerializedName("name")
    val name: String
)

/**
 * 映画の詳細情報
 */
data class MovieDetailDto(
    @SerializedName("id")
    val id: Int,

    @SerializedName("title")
    val title: String,

    @SerializedName("original_title")
    val originalTitle: String,

    @SerializedName("overview")
    val overview: String,

    @SerializedName("poster_path")
    val posterPath: String?,

    @SerializedName("backdrop_path")
    val backdropPath: String?,

    @SerializedName("release_date")
    val releaseDate: String,

    @SerializedName("vote_average")
    val voteAverage: Double,

    @SerializedName("vote_count")
    val voteCount: Int,

    @SerializedName("popularity")
    val popularity: Double,

    @SerializedName("genres")
    val genres: List<Genre>,

    @SerializedName("runtime")
    val runtime: Int?,

    @SerializedName("budget")
    val budget: Long,

    @SerializedName("revenue")
    val revenue: Long,

    @SerializedName("status")
    val status: String,

    @SerializedName("tagline")
    val tagline: String?
)