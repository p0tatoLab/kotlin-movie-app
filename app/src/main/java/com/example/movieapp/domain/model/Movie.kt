package com.example.movieapp.domain.model

/**
 * UI層で使用する映画モデル
 * DTOから変換され、UIに必要な情報のみを持つ
 */
data class Movie(
    val id: Int,
    val title: String,
    val overview: String,
    val posterUrl: String,
    val backdropUrl: String,
    val releaseDate: String,
    val voteAverage: Double,
    val voteCount: Int
)