package com.example.movieapp.data.local.dao

import androidx.room.*
import com.example.movieapp.data.local.entity.MovieDetailEntity
import com.example.movieapp.data.local.entity.MovieEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {

    // ========== Movies ==========

    /**
     * カテゴリ別に映画を取得
     */
    @Query("SELECT * FROM movies WHERE category = :category ORDER BY timestamp DESC")
    fun getMoviesByCategory(category: String): Flow<List<MovieEntity>>

    /**
     * 映画を挿入
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovies(movies: List<MovieEntity>)

    /**
     * カテゴリの映画を削除
     */
    @Query("DELETE FROM movies WHERE category = :category")
    suspend fun deleteMoviesByCategory(category: String)

    /**
     * 古いキャッシュを削除（24時間以上前）
     */
    @Query("DELETE FROM movies WHERE timestamp < :timestamp")
    suspend fun deleteOldMovies(timestamp: Long)

    // ========== Movie Details ==========

    /**
     * 映画詳細を取得
     */
    @Query("SELECT * FROM movie_details WHERE id = :movieId")
    suspend fun getMovieDetail(movieId: Int): MovieDetailEntity?

    /**
     * 映画詳細を取得（Flow）
     */
    @Query("SELECT * FROM movie_details WHERE id = :movieId")
    fun getMovieDetailFlow(movieId: Int): Flow<MovieDetailEntity?>

    /**
     * 映画詳細を挿入
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovieDetail(movieDetail: MovieDetailEntity)

    /**
     * 古い詳細キャッシュを削除（24時間以上前）
     */
    @Query("DELETE FROM movie_details WHERE timestamp < :timestamp")
    suspend fun deleteOldMovieDetails(timestamp: Long)
}