package com.example.movieapp.data.remote.api

import com.example.movieapp.data.remote.dto.MovieDetailDto
import com.example.movieapp.data.remote.dto.MovieResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * TMDb API サービスインターフェース
 */
interface TmdbApiService {

    /**
     * 人気映画を取得
     */
    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "ja-JP",
        @Query("page") page: Int = 1
    ): Response<MovieResponse>

    /**
     * 高評価映画を取得
     */
    @GET("movie/top_rated")
    suspend fun getTopRatedMovies(
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "ja-JP",
        @Query("page") page: Int = 1
    ): Response<MovieResponse>

    /**
     * 公開予定映画を取得
     */
    @GET("movie/upcoming")
    suspend fun getUpcomingMovies(
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "ja-JP",
        @Query("page") page: Int = 1
    ): Response<MovieResponse>

    /**
     * 映画を検索
     */
    @GET("search/movie")
    suspend fun searchMovies(
        @Query("api_key") apiKey: String,
        @Query("query") query: String,
        @Query("language") language: String = "ja-JP",
        @Query("page") page: Int = 1
    ): Response<MovieResponse>

    /**
     * 映画の詳細を取得
     */
    @GET("movie/{movie_id}")
    suspend fun getMovieDetail(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "ja-JP"
    ): Response<MovieDetailDto>

    companion object {
        const val BASE_URL = "https://api.themoviedb.org/3/"
    }
}