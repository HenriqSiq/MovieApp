package br.com.movieapp.core.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import br.com.movieapp.core.domain.model.MovieSearch
import br.com.movieapp.feature.search_movie.data.mapper.toMovieSearch
import br.com.movieapp.feature.search_movie.domain.source.MovieSearchRemoteDataSource

class MovieSearchPagingSource(
    private val query: String,
    private val remoteDataSource: MovieSearchRemoteDataSource
) : PagingSource<Int, MovieSearch>() {

    override fun getRefreshKey(state: PagingState<Int, MovieSearch>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage: LoadResult.Page<Int, MovieSearch>? = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(LIMIT) ?: anchorPage?.nextKey?.minus(LIMIT)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieSearch> {
        return try {
            val pageNumber = params.key ?: 1
            val response = remoteDataSource.getSearchMovies(page = pageNumber, query = query)
            val movies = response.results

            LoadResult.Page(
                data = movies.toMovieSearch(),
                prevKey = if(pageNumber == 1) null else pageNumber -1,
                nextKey = if(movies.isEmpty()) null else pageNumber +1
            )

        }catch (e: Exception){
            e.printStackTrace()
            return LoadResult.Error(e)
        }
    }

    companion object{
        private const val LIMIT = 20
    }
}