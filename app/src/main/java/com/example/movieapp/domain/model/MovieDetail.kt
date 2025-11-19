package com.example.movieapp.domain.model

/**
 * UI層で使用する映画詳細モデル
 */
data class MovieDetail(
    val id: Int,
    val title: String,
    val overview: String,
    val posterUrl: String,
    val backdropUrl: String,
    val releaseDate: String,
    val voteAverage: Double,
    val voteCount: Int,
    val genres: List<String>,
    val runtime: Int?,
    val tagline: String?
)