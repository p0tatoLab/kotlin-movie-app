package com.example.movieapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.movieapp.domain.model.Movie

@Entity(tableName = "movies")
data class MovieEntity(
    @PrimaryKey
    val id: Int,
    val title: String,
    val overview: String,
    val posterUrl: String,
    val backdropUrl: String,
    val releaseDate: String,
    val voteAverage: Double,
    val voteCount: Int,
    val category: String, // "popular", "top_rated", "upcoming"
    val timestamp: Long = System.currentTimeMillis()
)

/**
 * MovieEntity → Movie への変換
 */
fun MovieEntity.toDomainModel(): Movie {
    return Movie(
        id = id,
        title = title,
        overview = overview,
        posterUrl = posterUrl,
        backdropUrl = backdropUrl,
        releaseDate = releaseDate,
        voteAverage = voteAverage,
        voteCount = voteCount
    )
}

/**
 * Movie → MovieEntity への変換
 */
fun Movie.toEntity(category: String): MovieEntity {
    return MovieEntity(
        id = id,
        title = title,
        overview = overview,
        posterUrl = posterUrl,
        backdropUrl = backdropUrl,
        releaseDate = releaseDate,
        voteAverage = voteAverage,
        voteCount = voteCount,
        category = category
    )
}