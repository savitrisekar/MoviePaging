package com.example.movie.ui.details

import androidx.lifecycle.LiveData
import com.example.movie.data.api.ApiInterface
import com.example.movie.data.model.PopularDetails
import com.example.movie.data.repository.NetworkState
import com.example.movie.data.repository.PopularDetailsDataSource
import io.reactivex.disposables.CompositeDisposable

class PopularDetailsRepository(private val apiInterface: ApiInterface) {

    lateinit var upcomingMovieDataSource: PopularDetailsDataSource

    fun fetchSingleUpcomingDetails(
        compositeDisposable: CompositeDisposable,
        moveId: Int
    ): LiveData<PopularDetails> {
        upcomingMovieDataSource = PopularDetailsDataSource(apiInterface, compositeDisposable)
        upcomingMovieDataSource.fetchUpcomingDetails(moveId)

        return upcomingMovieDataSource.upcomingDetailsResponse
    }

    fun getUpcomingNetworkState(): LiveData<NetworkState> {
        return upcomingMovieDataSource.networkState
    }
}