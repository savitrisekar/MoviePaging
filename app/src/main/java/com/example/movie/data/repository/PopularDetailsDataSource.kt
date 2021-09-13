package com.example.movie.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.movie.data.api.ApiInterface
import com.example.movie.data.model.PopularDetails
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.lang.Exception

class PopularDetailsDataSource(
    private val apiInterface: ApiInterface,
    private val compositeDisposable: CompositeDisposable
) {
    //create mutable live data
    private val _networkState = MutableLiveData<NetworkState>() //private
    val networkState: LiveData<NetworkState> //public
        get() = _networkState //with this get, no need to implement get function to get networkState

    private val _upcomingDetailsResponse = MutableLiveData<PopularDetails>()
    val upcomingDetailsResponse: LiveData<PopularDetails>
        get() = _upcomingDetailsResponse

    fun fetchUpcomingDetails(movieId: Int) {
        _networkState.postValue(NetworkState.LOADING) //post the value of network state loading to mutable live data

        //using rxJava thread to make network calls
        try {
            compositeDisposable.add(apiInterface.getUpcomingDetails(movieId)
                .subscribeOn(Schedulers.io())
                .subscribe(
                    {
                        _upcomingDetailsResponse.postValue(it)
                        _networkState.postValue(NetworkState.LOADED)
                    },
                    {
                        _networkState.postValue(NetworkState.ERROR)
                        it.message?.let { it1 -> Log.e("DataSource", it1) }
                    }
                )
            )
        }
        catch (e: Exception) {
            e.message?.let { Log.e("DataSource", it) }
        }
    }
}