package com.example.movieapp.ui.state

import com.example.movieapp.domain.model.MovieDetail

/**
 * 映画詳細画面のState
 */
data class MovieDetailState(
    val movie: MovieDetail? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)