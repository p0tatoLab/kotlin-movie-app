package com.example.movieapp.ui.state

import com.example.movieapp.domain.model.Movie

/**
 * 映画一覧画面のState
 */
data class MovieListState(
    val movies: List<Movie> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)