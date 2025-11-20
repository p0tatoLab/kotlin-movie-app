package com.example.movieapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.movieapp.ui.components.ErrorView
import com.example.movieapp.ui.components.LoadingView
import com.example.movieapp.ui.components.MovieCard
import com.example.movieapp.viewmodel.MovieListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieListScreen(
    viewModel: MovieListViewModel = hiltViewModel(),
    onMovieClick: (Int) -> Unit,
    onSearchClick: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("人気", "高評価", "公開予定")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("映画") },
                actions = {
                    IconButton(onClick = onSearchClick) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "検索"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // タブ
            TabRow(selectedTabIndex = selectedTab) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = {
                            selectedTab = index
                            when (index) {
                                0 -> viewModel.loadPopularMovies()
                                1 -> viewModel.loadTopRatedMovies()
                                2 -> viewModel.loadUpcomingMovies()
                            }
                        },
                        text = { Text(title) }
                    )
                }
            }

            // コンテンツ
            Box(modifier = Modifier.fillMaxSize()) {
                when {
                    state.isLoading -> {
                        LoadingView()
                    }
                    state.error != null -> {
                        ErrorView(
                            message = state.error ?: "エラーが発生しました",
                            onRetry = {
                                when (selectedTab) {
                                    0 -> viewModel.loadPopularMovies()
                                    1 -> viewModel.loadTopRatedMovies()
                                    2 -> viewModel.loadUpcomingMovies()
                                }
                            }
                        )
                    }
                    state.movies.isEmpty() -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = androidx.compose.ui.Alignment.Center
                        ) {
                            Text(
                                text = "映画が見つかりません",
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                    else -> {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(
                                items = state.movies,
                                key = { movie -> movie.id }
                            ) { movie ->
                                MovieCard(
                                    movie = movie,
                                    onClick = { onMovieClick(movie.id) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}