package com.example.movie.data.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.example.movie.data.api.ApiInterface
import com.example.movie.data.api.FIRST_PAGE
import com.example.movie.data.model.PopularMovie
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class PopularDataSource(
    private val apiInterface: ApiInterface,
    private val compositeDisposable: CompositeDisposable
) : PageKeyedDataSource<Int, PopularMovie>() {

    private var page = FIRST_PAGE
    val networkState: MutableLiveData<NetworkState> = MutableLiveData()

    //load initial data for request first page
    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, PopularMovie>
    ) {
        networkState.postValue(NetworkState.LOADING)
        compositeDisposable.add(
            apiInterface.getUpcomingMovie(page)
                .subscribeOn(Schedulers.io())
                .subscribe(
                    {
                        callback.onResult(it.movieList, null, page + 1)
                        networkState.postValue(NetworkState.LOADED)
                    },
                    {
                        networkState.postValue(NetworkState.ERROR)
                        it.message?.let { it1 -> Log.e("DataSource", it1) }
                    }
                )
        )
    }

    //load next page, will be called when user scroll down
    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, PopularMovie>) {
        networkState.postValue(NetworkState.LOADING)
        compositeDisposable.add(
            apiInterface.getUpcomingMovie(params.key)
                .subscribeOn(Schedulers.io())
                .subscribe(
                    {
                        if (it.totalPages >= params.key) {
                            callback.onResult(it.movieList, params.key + 1)
                            networkState.postValue(NetworkState.LOADED)
                        } else {
                            networkState.postValue(NetworkState.ENLIST)
                        }
                    },
                    {
                        networkState.postValue(NetworkState.ERROR)
                        it.message?.let { it1 -> Log.e("DataSource", it1) }

                    }
                )
        )
    }

    //load the previous page, will be called when user scrolls up but recyclerView will hold the previous data
    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, PopularMovie>) {

    }
}