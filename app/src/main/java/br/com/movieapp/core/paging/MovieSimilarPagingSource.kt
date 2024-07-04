package br.com.movieapp.core.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import br.com.movieapp.core.domain.model.Movie
import br.com.movieapp.feature.movie_detail.domain.source.MovieDetailsRemoteDataSource
import br.com.movieapp.feature.movie_popular.data.mapper.toMovie

class MovieSimilarPagingSource(
    private val remoteDataSource: MovieDetailsRemoteDataSource,
    private val movieId: Int
):PagingSource<Int, Movie>(){

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return state.anchorPosition?.let{ anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(LIMIT) ?: anchorPage?.nextKey?.minus(LIMIT)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        return try{
            val pageNumber = params.loadSize ?: 1
            val response = remoteDataSource.getMoviesSimilar(
                page = pageNumber,
                movieId = movieId
            )
            val movies = response.results

            LoadResult.Page(
                data = movies.toMovie(),
                prevKey = if(pageNumber == 1) null else pageNumber - 1,
                nextKey = if(movies.isEmpty()) null else pageNumber + 1
            )


        }catch (e: Exception){
            e.printStackTrace();
            return LoadResult.Error(e)
        }
    }

    companion object{
        private const val LIMIT = 20
    }
}