package com.example.movie.ui.popular

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.movie.data.repository.PopularRepository

class PopularViewModel(private val popularRepository: PopularRepository = PopularRepository()) :
    ViewModel() {

    fun movieList() = popularRepository.getPopularMovie().cachedIn(viewModelScope)
}