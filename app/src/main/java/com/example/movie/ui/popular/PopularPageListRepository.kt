package com.example.movie.ui.popular

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.movie.data.api.ApiInterface
import com.example.movie.data.api.POST_PER_PAGE
import com.example.movie.data.model.PopularMovie
import com.example.movie.data.repository.NetworkState
import com.example.movie.data.repository.PopularDataSource
import com.example.movie.data.repository.PopularDataSourceFactory
import io.reactivex.disposables.CompositeDisposable

class PopularPageListRepository(private val apiInterface: ApiInterface) {

    lateinit var upcomingPagedList: LiveData<PagedList<PopularMovie>>
    lateinit var upcomingDataSourceFactory: PopularDataSourceFactory

    fun fetchLiveUpcomingPagedList(compositeDisposable: CompositeDisposable): LiveData<PagedList<PopularMovie>> {
        upcomingDataSourceFactory = PopularDataSourceFactory(apiInterface, compositeDisposable)

        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(POST_PER_PAGE)
            .build()

        upcomingPagedList = LivePagedListBuilder(upcomingDataSourceFactory, config).build()
        return upcomingPagedList
    }

    fun getNetworkState(): LiveData<NetworkState> {
        return Transformations.switchMap<PopularDataSource, NetworkState>(
            upcomingDataSourceFactory.upcomingLiveDataSource, PopularDataSource::networkState
        )
    }
}