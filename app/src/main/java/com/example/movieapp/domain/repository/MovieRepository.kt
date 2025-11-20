package com.example.movieapp.domain.repository

import com.example.movieapp.domain.model.Movie
import com.example.movieapp.domain.model.MovieDetail
import com.example.movieapp.util.Result
import kotlinx.coroutines.flow.Flow

/**
 * 映画データのRepository インターフェース
 * データソースの抽象化
 */
interface MovieRepository {

    /**
     * 人気映画を取得
     */
    fun getPopularMovies(page: Int = 1): Flow<Result<List<Movie>>>

    /**
     * 高評価映画を取得
     */
    fun getTopRatedMovies(page: Int = 1): Flow<Result<List<Movie>>>

    /**
     * 公開予定映画を取得
     */
    fun getUpcomingMovies(page: Int = 1): Flow<Result<List<Movie>>>

    /**
     * 映画を検索
     */
    fun searchMovies(query: String, page: Int = 1): Flow<Result<List<Movie>>>

    /**
     * 映画の詳細を取得
     */
    fun getMovieDetail(movieId: Int): Flow<Result<MovieDetail>>
}