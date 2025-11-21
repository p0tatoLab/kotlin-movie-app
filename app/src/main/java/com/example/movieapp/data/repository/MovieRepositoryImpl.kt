package com.example.movieapp.data.repository

import com.example.movieapp.BuildConfig
import com.example.movieapp.data.local.dao.MovieDao
import com.example.movieapp.data.local.entity.toDomainModel
import com.example.movieapp.data.local.entity.toEntity
import com.example.movieapp.data.remote.api.TmdbApiService
import com.example.movieapp.data.remote.dto.toDomainModel
import com.example.movieapp.domain.model.Movie
import com.example.movieapp.domain.model.MovieDetail
import com.example.movieapp.domain.repository.MovieRepository
import com.example.movieapp.util.NetworkException
import com.example.movieapp.util.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import java.io.IOException
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieRepositoryImpl @Inject constructor(
    private val apiService: TmdbApiService,
    private val movieDao: MovieDao
) : MovieRepository {

    private val apiKey = BuildConfig.TMDB_API_KEY

    // キャッシュの有効期限（24時間）
    private val cacheValidityDuration = TimeUnit.HOURS.toMillis(24)

    override fun getPopularMovies(page: Int): Flow<Result<List<Movie>>> = flow {
        emit(Result.Loading)

        try {
            // まずキャッシュから取得
            val cachedMovies = movieDao.getMoviesByCategory("popular").first()

            if (cachedMovies.isNotEmpty() && isCacheValid(cachedMovies.first().timestamp)) {
                // キャッシュが有効な場合
                emit(Result.Success(cachedMovies.map { it.toDomainModel() }))
            } else {
                // APIから取得
                val response = apiService.getPopularMovies(apiKey, page = page)
                val result = handleResponse(response)

                if (result is Result.Success) {
                    // キャッシュに保存
                    movieDao.deleteMoviesByCategory("popular")
                    movieDao.insertMovies(result.data.map { it.toEntity("popular") })
                }

                emit(result)
            }
        } catch (e: Exception) {
            // エラー時もキャッシュがあれば返す
            val cachedMovies = movieDao.getMoviesByCategory("popular").first()
            if (cachedMovies.isNotEmpty()) {
                emit(Result.Success(cachedMovies.map { it.toDomainModel() }))
            } else {
                emit(Result.Error(handleException(e)))
            }
        }
    }

    override fun getTopRatedMovies(page: Int): Flow<Result<List<Movie>>> = flow {
        emit(Result.Loading)

        try {
            val cachedMovies = movieDao.getMoviesByCategory("top_rated").first()

            if (cachedMovies.isNotEmpty() && isCacheValid(cachedMovies.first().timestamp)) {
                emit(Result.Success(cachedMovies.map { it.toDomainModel() }))
            } else {
                val response = apiService.getTopRatedMovies(apiKey, page = page)
                val result = handleResponse(response)

                if (result is Result.Success) {
                    movieDao.deleteMoviesByCategory("top_rated")
                    movieDao.insertMovies(result.data.map { it.toEntity("top_rated") })
                }

                emit(result)
            }
        } catch (e: Exception) {
            val cachedMovies = movieDao.getMoviesByCategory("top_rated").first()
            if (cachedMovies.isNotEmpty()) {
                emit(Result.Success(cachedMovies.map { it.toDomainModel() }))
            } else {
                emit(Result.Error(handleException(e)))
            }
        }
    }

    override fun getUpcomingMovies(page: Int): Flow<Result<List<Movie>>> = flow {
        emit(Result.Loading)

        try {
            val cachedMovies = movieDao.getMoviesByCategory("upcoming").first()

            if (cachedMovies.isNotEmpty() && isCacheValid(cachedMovies.first().timestamp)) {
                emit(Result.Success(cachedMovies.map { it.toDomainModel() }))
            } else {
                val response = apiService.getUpcomingMovies(apiKey, page = page)
                val result = handleResponse(response)

                if (result is Result.Success) {
                    movieDao.deleteMoviesByCategory("upcoming")
                    movieDao.insertMovies(result.data.map { it.toEntity("upcoming") })
                }

                emit(result)
            }
        } catch (e: Exception) {
            val cachedMovies = movieDao.getMoviesByCategory("upcoming").first()
            if (cachedMovies.isNotEmpty()) {
                emit(Result.Success(cachedMovies.map { it.toDomainModel() }))
            } else {
                emit(Result.Error(handleException(e)))
            }
        }
    }

    override fun searchMovies(query: String, page: Int): Flow<Result<List<Movie>>> = flow {
        emit(Result.Loading)
        try {
            val response = apiService.searchMovies(apiKey, query = query, page = page)
            emit(handleResponse(response))
        } catch (e: Exception) {
            emit(Result.Error(handleException(e)))
        }
    }

    override fun getMovieDetail(movieId: Int): Flow<Result<MovieDetail>> = flow {
        emit(Result.Loading)

        try {
            // まずキャッシュから取得
            val cachedDetail = movieDao.getMovieDetail(movieId)

            if (cachedDetail != null && isCacheValid(cachedDetail.timestamp)) {
                // キャッシュが有効な場合
                emit(Result.Success(cachedDetail.toDomainModel()))
            } else {
                // APIから取得
                val response = apiService.getMovieDetail(movieId, apiKey)

                if (response.isSuccessful) {
                    response.body()?.let { dto ->
                        val movieDetail = dto.toDomainModel()

                        // キャッシュに保存
                        movieDao.insertMovieDetail(movieDetail.toEntity())

                        emit(Result.Success(movieDetail))
                    } ?: emit(Result.Error(NetworkException.UnknownError("データが見つかりません")))
                } else {
                    emit(Result.Error(handleHttpError(response.code())))
                }
            }
        } catch (e: Exception) {
            // エラー時もキャッシュがあれば返す
            val cachedDetail = movieDao.getMovieDetail(movieId)
            if (cachedDetail != null) {
                emit(Result.Success(cachedDetail.toDomainModel()))
            } else {
                emit(Result.Error(handleException(e)))
            }
        }
    }

    /**
     * キャッシュが有効かチェック
     */
    private fun isCacheValid(timestamp: Long): Boolean {
        return System.currentTimeMillis() - timestamp < cacheValidityDuration
    }

    /**
     * Retrofitのレスポンスを処理
     */
    private fun handleResponse(response: Response<com.example.movieapp.data.remote.dto.MovieResponse>): Result<List<Movie>> {
        return if (response.isSuccessful) {
            response.body()?.let { movieResponse ->
                Result.Success(movieResponse.results.map { it.toDomainModel() })
            } ?: Result.Error(NetworkException.UnknownError("データが見つかりません"))
        } else {
            Result.Error(handleHttpError(response.code()))
        }
    }

    /**
     * HTTPエラーコードを処理
     */
    private fun handleHttpError(code: Int): NetworkException {
        return when (code) {
            in 400..499 -> NetworkException.NetworkError("クライアントエラー (${code})")
            in 500..599 -> NetworkException.ServerError("サーバーエラー (${code})")
            else -> NetworkException.UnknownError("HTTPエラー (${code})")
        }
    }

    /**
     * 例外を処理
     */
    private fun handleException(exception: Exception): NetworkException {
        return when (exception) {
            is IOException -> NetworkException.NoConnectionError()
            else -> NetworkException.UnknownError(exception.message ?: "不明なエラー")
        }
    }
}