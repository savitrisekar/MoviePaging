package com.example.movie.ui.popular

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.example.movie.data.model.PopularMovie
import com.example.movie.data.repository.NetworkState
import io.reactivex.disposables.CompositeDisposable

class PopularViewModel(private val mainPageListRepository: PopularPageListRepository) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    val upcomingPagedList: LiveData<PagedList<PopularMovie>> by lazy {
        mainPageListRepository.fetchLiveUpcomingPagedList(compositeDisposable)
    }

    val networkState: LiveData<NetworkState> by lazy {
        mainPageListRepository.getNetworkState()
    }

    fun listIsEmpty(): Boolean {
        return upcomingPagedList.value?.isEmpty() ?: true
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}