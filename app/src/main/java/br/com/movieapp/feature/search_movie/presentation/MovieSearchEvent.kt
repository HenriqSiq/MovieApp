package br.com.movieapp.feature.search_movie.presentation

sealed class MovieSearchEvent {
    data class EnteredQuery(val value: String): MovieSearchEvent()
}