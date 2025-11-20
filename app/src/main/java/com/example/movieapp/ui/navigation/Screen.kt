package com.example.movieapp.ui.navigation

sealed class Screen(val route: String) {
    object MovieList : Screen("movie_list")
    object MovieDetail : Screen("movie_detail/{movieId}") {
        fun createRoute(movieId: Int): String {
            return "movie_detail/$movieId"
        }
    }
    object Search : Screen("search")
}