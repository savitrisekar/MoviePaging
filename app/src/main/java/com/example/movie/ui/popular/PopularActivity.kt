package com.example.movie.ui.popular

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.*
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.example.movie.databinding.ActivityPopularBinding
import com.example.movie.ui.popular.adapter.PopularAdapter
import com.example.movie.ui.popular.adapter.StateAdapter
import com.example.movie.utils.visibleWhen
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

    private lateinit var popularAdapter: PopularAdapter
    private lateinit var binding: ActivityPopularBinding
    private var fetchMovieJob: Job? = null

    private val viewModel by lazy {
        ViewModelProvider(this)[PopularViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPopularBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initAdapter()
        fetchMovie()
    }

    private fun initAdapter() {

        popularAdapter = PopularAdapter(viewModel = viewModel)
        binding.rvPopular.adapter = popularAdapter

        val headerAdapter = StateAdapter { popularAdapter.retry() }
        val footerAdapter = StateAdapter { popularAdapter.retry() }

        val concatAdapter = popularAdapter.withLoadStateHeaderAndFooter(
            header = headerAdapter,
            footer = footerAdapter
        )

        binding.rvPopular.adapter = concatAdapter
        val gridLayoutManager = GridLayoutManager(this, 2)
        binding.rvPopular.layoutManager = gridLayoutManager

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
                binding.tvError.visibleWhen(it is LoadState.Error)
                binding.btnRetry.visibleWhen(it is LoadState.Error)
                binding.progressBar.visibleWhen(it is LoadState.Loading)
                binding.rvPopular.visibleWhen(it is LoadState.NotLoading)
            }
        }

        binding.btnRetry.setOnClickListener {
            popularAdapter.refresh()
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