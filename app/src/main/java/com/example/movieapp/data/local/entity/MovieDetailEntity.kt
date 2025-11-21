package com.example.movieapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.example.movieapp.domain.model.MovieDetail
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@Entity(tableName = "movie_details")
data class MovieDetailEntity(
    @PrimaryKey
    val id: Int,
    val title: String,
    val overview: String,
    val posterUrl: String,
    val backdropUrl: String,
    val releaseDate: String,
    val voteAverage: Double,
    val voteCount: Int,
    val genres: String, // JSON文字列として保存
    val runtime: Int?,
    val tagline: String?,
    val timestamp: Long = System.currentTimeMillis()
)

/**
 * MovieDetailEntity → MovieDetail への変換
 */
fun MovieDetailEntity.toDomainModel(): MovieDetail {
    val genresList = Gson().fromJson<List<String>>(
        genres,
        object : TypeToken<List<String>>() {}.type
    )

    return MovieDetail(
        id = id,
        title = title,
        overview = overview,
        posterUrl = posterUrl,
        backdropUrl = backdropUrl,
        releaseDate = releaseDate,
        voteAverage = voteAverage,
        voteCount = voteCount,
        genres = genresList,
        runtime = runtime,
        tagline = tagline
    )
}

/**
 * MovieDetail → MovieDetailEntity への変換
 */
fun MovieDetail.toEntity(): MovieDetailEntity {
    return MovieDetailEntity(
        id = id,
        title = title,
        overview = overview,
        posterUrl = posterUrl,
        backdropUrl = backdropUrl,
        releaseDate = releaseDate,
        voteAverage = voteAverage,
        voteCount = voteCount,
        genres = Gson().toJson(genres),
        runtime = runtime,
        tagline = tagline
    )
}