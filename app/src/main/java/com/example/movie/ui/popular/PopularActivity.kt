package com.example.movie.ui.popular

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.example.movie.R
import com.example.movie.data.api.ApiClient
import com.example.movie.data.api.ApiInterface
import com.example.movie.data.repository.NetworkState
import kotlinx.android.synthetic.main.activity_popular.*

class PopularActivity : AppCompatActivity() {

    private lateinit var viewModel: PopularViewModel
    lateinit var mainPageListRepository: PopularPageListRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_popular)

        val apiInterface: ApiInterface = ApiClient.getClient()
        mainPageListRepository =
            PopularPageListRepository(apiInterface)
        viewModel = getViewModel()

        val upcomingAdapter = PopularPagedAdapter(this)
        val gridLayoutManager = GridLayoutManager(this, 2)
        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                val viewType = upcomingAdapter.getItemViewType(position)
                return if (viewType == upcomingAdapter.UPCOMING_VIEW_TYPE) 1  //UPCOMING_VIEW_TYPE will occupy 1 out of 2 span
                else 2                                                  //NETWORK_VIEW_TYPE will occupy all 2 span
            }
        }

        //set recyclerView
        rv_popular.layoutManager = gridLayoutManager
        rv_popular.setHasFixedSize(true)
        rv_popular.adapter = upcomingAdapter

        viewModel.upcomingPagedList.observe(this, Observer {
            upcomingAdapter.submitList(it)
        })

        viewModel.networkState.observe(this, Observer {

            progressBar.visibility =
                if (viewModel.listIsEmpty() && it == NetworkState.LOADED) View.VISIBLE else View.GONE
            tv_error.visibility =
                if (viewModel.listIsEmpty() && it == NetworkState.ERROR) View.VISIBLE else View.GONE

            if (!viewModel.listIsEmpty()) {
                upcomingAdapter.setNetworkState(it)
            }
        })
    }

    private fun getViewModel(): PopularViewModel {
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return PopularViewModel(
                    mainPageListRepository
                ) as T
            }
        })[PopularViewModel::class.java]
    }
}