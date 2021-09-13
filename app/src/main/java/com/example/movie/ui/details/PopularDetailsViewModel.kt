package com.example.movie.ui.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.movie.data.model.PopularDetails
import com.example.movie.data.repository.NetworkState
import io.reactivex.disposables.CompositeDisposable

class PopularDetailsViewModel(
    private val upcomingDetailsRepository: PopularDetailsRepository,
    movieId: Int
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    //fetch upcoming movie details by lazy
    val upcomingDetails: LiveData<PopularDetails> by lazy {
        upcomingDetailsRepository.fetchSingleUpcomingDetails(compositeDisposable, movieId)
    }

    val networkState: LiveData<NetworkState> by lazy {
        upcomingDetailsRepository.getUpcomingNetworkState()
    }

    override fun onCleared() { //called when the activity or fragment get destroyed
        super.onCleared()
        compositeDisposable.dispose()
    }
}