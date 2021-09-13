package com.example.movie.data.repository

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.example.movie.data.api.ApiInterface
import com.example.movie.data.model.PopularMovie
import io.reactivex.disposables.CompositeDisposable

class PopularDataSourceFactory(
    private val apiInterface: ApiInterface,
    private val compositeDisposable: CompositeDisposable
) : DataSource.Factory<Int, PopularMovie>() {

    val upcomingLiveDataSource = MutableLiveData<PopularDataSource>()

    override fun create(): DataSource<Int, PopularMovie> {
        val upcomingDataSource = PopularDataSource(apiInterface, compositeDisposable)

        upcomingLiveDataSource.postValue(upcomingDataSource)
        return upcomingDataSource
    }
}