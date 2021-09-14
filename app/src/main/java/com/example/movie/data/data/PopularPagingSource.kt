package com.example.movie.data.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.movie.data.api.ApiClient
import com.example.movie.data.api.ApiService
import com.example.movie.data.model.PopularModel
import com.example.movie.data.model.PopularResponse

/**
 * Data sumber
 */

class PopularPagingSource(private val apiService: ApiService = ApiClient.build()) :
    PagingSource<Int, PopularModel>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PopularModel> {
        val pagePosition = params.key ?: FIRST_PAGE

        return try {
            val data = apiService.getPopularMovie(
                API_KEY,
                LANGUAGE,
                pagePosition
            ).movieList //using retrofit
            val prevKey = if (pagePosition == FIRST_PAGE) null else pagePosition - 1
            val nextKey = if (data.isEmpty()) null else pagePosition + 1

            LoadResult.Page(data, prevKey, nextKey)
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }

    /**
     * The refresh key is used for subsequent calls to PagingSource.Load after the initial load.
     */

    override fun getRefreshKey(state: PagingState<Int, PopularModel>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    companion object {
        private const val FIRST_PAGE = 1
        private const val API_KEY = "40bb9b419730b4e8c5b34f37897995d0"
        private const val LANGUAGE = "en-EN"
    }
}