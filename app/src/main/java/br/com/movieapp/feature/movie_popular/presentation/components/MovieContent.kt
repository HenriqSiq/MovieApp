package br.com.movieapp.feature.movie_popular.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import br.com.movieapp.core.domain.model.Movie
import br.com.movieapp.core.presentation.components.common.ErrorScreen
import br.com.movieapp.core.presentation.components.common.LoadingView

@Composable
fun MovieContent(
    modifier: Modifier = Modifier,
    pagingMovies: LazyPagingItems<Movie>,
    paddingValues: PaddingValues,
    onClick: (movieId: Int) -> Unit
) {
    Box(
        modifier = modifier.background(Color.Black)
    ){
        LazyVerticalGrid(
            columns = GridCells.Fixed(count = 3),
            contentPadding = paddingValues,
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            items(pagingMovies.itemCount){ index ->
                val movie = pagingMovies[index]
                movie?.let{
                    MovieItem(
                        voteAverage = it.voteAverage,
                        imageUrl = it.imageUrl,
                        id = it.id,
                        onClick = { movieId ->
                            onClick(movieId)
                        }
                    )
                }
                pagingMovies.apply {
                    when{
                        loadState.refresh is LoadState.Loading -> {
                            this@LazyVerticalGrid.item{
                                LoadingView()
                            }
                        }
                        loadState.append is LoadState.Loading -> {
                            this@LazyVerticalGrid.item{
                                LoadingView()
                            }
                        }
                        loadState.refresh is LoadState.Error -> {
                            this@LazyVerticalGrid.item {
                                ErrorScreen(
                                    message = "Verifique sua conexão com a internet",
                                    retry = ::retry
                                )
                            }
                        }
                        loadState.append is LoadState.Error -> {
                            this@LazyVerticalGrid.item {
                                ErrorScreen(
                                    message = "Verifique sua conexão com a internet",
                                    retry = ::retry
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}