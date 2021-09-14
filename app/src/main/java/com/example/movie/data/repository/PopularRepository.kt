package com.example.movie.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.movie.data.data.PopularPagingSource
import com.example.movie.data.model.PopularModel
import kotlinx.coroutines.flow.Flow

/**
 * Configure PagingData
 * */

class PopularRepository {
    fun getPopularMovie(): Flow<PagingData<PopularModel>> {
        val moviePagingConfig = PagingConfig(pageSize = 10)
        val moviePagingFactory = { PopularPagingSource() }

        return Pager(config = moviePagingConfig, pagingSourceFactory = moviePagingFactory).flow
    }
}