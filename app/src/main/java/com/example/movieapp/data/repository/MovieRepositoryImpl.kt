package com.example.movieapp.data.repository

import com.example.movieapp.BuildConfig
import com.example.movieapp.data.remote.api.TmdbApiService
import com.example.movieapp.data.remote.dto.toDomainModel
import com.example.movieapp.domain.model.Movie
import com.example.movieapp.domain.model.MovieDetail
import com.example.movieapp.domain.repository.MovieRepository
import com.example.movieapp.util.NetworkException
import com.example.movieapp.util.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieRepositoryImpl @Inject constructor(
    private val apiService: TmdbApiService
) : MovieRepository {

    private val apiKey = BuildConfig.TMDB_API_KEY

    override fun getPopularMovies(page: Int): Flow<Result<List<Movie>>> = flow {
        emit(Result.Loading)
        try {
            val response = apiService.getPopularMovies(apiKey, page = page)
            emit(handleResponse(response))
        } catch (e: Exception) {
            emit(Result.Error(handleException(e)))
        }
    }

    override fun getTopRatedMovies(page: Int): Flow<Result<List<Movie>>> = flow {
        emit(Result.Loading)
        try {
            val response = apiService.getTopRatedMovies(apiKey, page = page)
            emit(handleResponse(response))
        } catch (e: Exception) {
            emit(Result.Error(handleException(e)))
        }
    }

    override fun getUpcomingMovies(page: Int): Flow<Result<List<Movie>>> = flow {
        emit(Result.Loading)
        try {
            val response = apiService.getUpcomingMovies(apiKey, page = page)
            emit(handleResponse(response))
        } catch (e: Exception) {
            emit(Result.Error(handleException(e)))
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
            val response = apiService.getMovieDetail(movieId, apiKey)
            if (response.isSuccessful) {
                response.body()?.let { dto ->
                    emit(Result.Success(dto.toDomainModel()))
                } ?: emit(Result.Error(NetworkException.UnknownError("データが見つかりません")))
            } else {
                emit(Result.Error(handleHttpError(response.code())))
            }
        } catch (e: Exception) {
            emit(Result.Error(handleException(e)))
        }
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