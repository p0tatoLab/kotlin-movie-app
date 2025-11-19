package com.example.movieapp.data.remote.dto

import com.example.movieapp.domain.model.Movie
import com.example.movieapp.domain.model.MovieDetail

/**
 * TMDb画像ベースURL
 */
private const val IMAGE_BASE_URL = "https://image.tmdb.org/t/p/"
private const val POSTER_SIZE = "w500"
private const val BACKDROP_SIZE = "w780"

/**
 * MovieDto → Movie への変換
 */
fun MovieDto.toDomainModel(): Movie {
    return Movie(
        id = id,
        title = title,
        overview = overview,
        posterUrl = posterPath?.let { "$IMAGE_BASE_URL$POSTER_SIZE$it" } ?: "",
        backdropUrl = backdropPath?.let { "$IMAGE_BASE_URL$BACKDROP_SIZE$it" } ?: "",
        releaseDate = releaseDate,
        voteAverage = voteAverage,
        voteCount = voteCount
    )
}

/**
 * MovieDetailDto → MovieDetail への変換
 */
fun MovieDetailDto.toDomainModel(): MovieDetail {
    return MovieDetail(
        id = id,
        title = title,
        overview = overview,
        posterUrl = posterPath?.let { "$IMAGE_BASE_URL$POSTER_SIZE$it" } ?: "",
        backdropUrl = backdropPath?.let { "$IMAGE_BASE_URL$BACKDROP_SIZE$it" } ?: "",
        releaseDate = releaseDate,
        voteAverage = voteAverage,
        voteCount = voteCount,
        genres = genres.map { it.name },
        runtime = runtime,
        tagline = tagline
    )
}