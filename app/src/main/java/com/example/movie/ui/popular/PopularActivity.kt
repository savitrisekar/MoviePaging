package com.example.movie.ui.popular

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.*
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.example.movie.R
import com.example.movie.ui.popular.adapter.PopularAdapter
import com.example.movie.ui.popular.adapter.StateAdapter
import com.example.movie.visibleWhen
import kotlinx.android.synthetic.main.activity_popular.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * reference :
 * https://medium.com/@ekoo/mencoba-paging-3-0-f73221ae1d97
 *
 * for centered inside recyclerView gridLayout :
 * https://stackoverflow.com/questions/65291291/android-loadstateadapter-not-centered-inside-recyclerview-gridlayout
 *
 * */

class PopularActivity : AppCompatActivity() {

    private var fetchMovieJob: Job? = null
    private val popularAdapter: PopularAdapter by lazy {
        PopularAdapter()
    }

    private val viewModel by lazy {
        ViewModelProvider(this)[PopularViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_popular)

        initAdapter()
        fetchMovie()

        btn_retry.setOnClickListener {
            popularAdapter.retry()
        }
    }

    private fun initAdapter() {

        val headerAdapter = StateAdapter { popularAdapter.retry() }
        val footerAdapter = StateAdapter { popularAdapter.retry() }

        val concatAdapter = popularAdapter.withLoadStateHeaderAndFooter(
            header = headerAdapter,
            footer = footerAdapter
        )

        rv_popular.adapter = concatAdapter
        val gridLayoutManager = GridLayoutManager(this, 2)
        rv_popular.layoutManager = gridLayoutManager

        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (position == 0 && headerAdapter.itemCount > 0) {
                    // if it is the first position and we have a header,
                    2
                } else if (position == concatAdapter.itemCount - 1 && footerAdapter.itemCount > 0) {
                    // if it is the last position and we have a footer
                    2
                } else {
                    1
                }
            }
        }

        popularAdapter.addLoadStateListener { loadState ->
            loadState.refresh.let {
                tv_error.visibleWhen(it is LoadState.Error)
                btn_retry.visibleWhen(it is LoadState.Error)
                progressBar.visibleWhen(it is LoadState.Loading)
                rv_popular.visibleWhen(it is LoadState.NotLoading)
            }
        }
    }

    private fun fetchMovie() {
        fetchMovieJob?.cancel()
        fetchMovieJob = lifecycleScope.launch {
            viewModel.movieList().collectLatest {
                popularAdapter.submitData(it)
            }
        }
    }
}