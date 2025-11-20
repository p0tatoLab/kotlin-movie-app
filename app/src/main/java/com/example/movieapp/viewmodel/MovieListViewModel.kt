package com.example.movieapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieapp.domain.repository.MovieRepository
import com.example.movieapp.ui.state.MovieListState
import com.example.movieapp.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieListViewModel @Inject constructor(
    private val repository: MovieRepository
) : ViewModel() {

    private val _state = MutableStateFlow(MovieListState())
    val state: StateFlow<MovieListState> = _state.asStateFlow()

    init {
        loadPopularMovies()
    }

    /**
     * 人気映画を読み込む
     */
    fun loadPopularMovies() {
        viewModelScope.launch {
            repository.getPopularMovies().collect { result ->
                when (result) {
                    is Result.Loading -> {
                        _state.value = _state.value.copy(isLoading = true, error = null)
                    }
                    is Result.Success -> {
                        _state.value = _state.value.copy(
                            movies = result.data,
                            isLoading = false,
                            error = null
                        )
                    }
                    is Result.Error -> {
                        _state.value = _state.value.copy(
                            isLoading = false,
                            error = result.exception.message
                        )
                    }
                }
            }
        }
    }

    /**
     * 高評価映画を読み込む
     */
    fun loadTopRatedMovies() {
        viewModelScope.launch {
            repository.getTopRatedMovies().collect { result ->
                when (result) {
                    is Result.Loading -> {
                        _state.value = _state.value.copy(isLoading = true, error = null)
                    }
                    is Result.Success -> {
                        _state.value = _state.value.copy(
                            movies = result.data,
                            isLoading = false,
                            error = null
                        )
                    }
                    is Result.Error -> {
                        _state.value = _state.value.copy(
                            isLoading = false,
                            error = result.exception.message
                        )
                    }
                }
            }
        }
    }

    /**
     * 公開予定映画を読み込む
     */
    fun loadUpcomingMovies() {
        viewModelScope.launch {
            repository.getUpcomingMovies().collect { result ->
                when (result) {
                    is Result.Loading -> {
                        _state.value = _state.value.copy(isLoading = true, error = null)
                    }
                    is Result.Success -> {
                        _state.value = _state.value.copy(
                            movies = result.data,
                            isLoading = false,
                            error = null
                        )
                    }
                    is Result.Error -> {
                        _state.value = _state.value.copy(
                            isLoading = false,
                            error = result.exception.message
                        )
                    }
                }
            }
        }
    }

    /**
     * 映画を検索
     */
    fun searchMovies(query: String) {
        if (query.isBlank()) {
            loadPopularMovies()
            return
        }

        viewModelScope.launch {
            repository.searchMovies(query).collect { result ->
                when (result) {
                    is Result.Loading -> {
                        _state.value = _state.value.copy(isLoading = true, error = null)
                    }
                    is Result.Success -> {
                        _state.value = _state.value.copy(
                            movies = result.data,
                            isLoading = false,
                            error = null
                        )
                    }
                    is Result.Error -> {
                        _state.value = _state.value.copy(
                            isLoading = false,
                            error = result.exception.message
                        )
                    }
                }
            }
        }
    }
}