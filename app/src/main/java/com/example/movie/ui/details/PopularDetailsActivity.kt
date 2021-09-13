package com.example.movie.ui.details

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.example.movie.R
import com.example.movie.data.api.ApiClient
import com.example.movie.data.api.ApiInterface
import com.example.movie.data.api.POSTER_BASE_URL
import com.example.movie.data.model.PopularDetails
import com.example.movie.data.repository.NetworkState
import kotlinx.android.synthetic.main.activity_popular_details.*

class PopularDetailsActivity : AppCompatActivity() {

    private lateinit var viewModel: PopularDetailsViewModel
    private lateinit var upcomingDetailsRepository: PopularDetailsRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_popular_details)

        val movieId: Int = intent.getIntExtra("id", 1)

        val apiInterface : ApiInterface = ApiClient.getClient()
        upcomingDetailsRepository = PopularDetailsRepository(apiInterface)

        viewModel = getViewModel(movieId)
        //observe
        viewModel.upcomingDetails.observe(this, Observer {
            bindUI(it)
        })

        //observe network state
        viewModel.networkState.observe(this, Observer {
            progressBar.visibility = if (it == NetworkState.LOADING) View.VISIBLE else View.GONE
            tv_error.visibility = if (it == NetworkState.ERROR) View.VISIBLE else View.GONE
        })
    }

    private fun bindUI(it: PopularDetails) {
        tv_title_movie.text = it.title
        tv_tagline.text = it.tagline
        tv_date.text = it.releaseDate
        tv_rating.text = it.rating.toString()
        tv_overview.text = it.overview

        val imgMovie : String = POSTER_BASE_URL + it.posterPath
        Glide.with(this)
            .load(imgMovie)
            .into(iv_poster_movie)
    }

    fun getViewModel(movieId: Int): PopularDetailsViewModel {
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return PopularDetailsViewModel(upcomingDetailsRepository, movieId) as T
            }
        })[PopularDetailsViewModel::class.java]
    }
}