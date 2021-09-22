package com.example.movie.ui.popular.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.movie.R
import com.example.movie.databinding.ItemLoadStateBinding
import com.example.movie.utils.visibleWhen

class StateAdapter(private val retry: () -> Unit) :
    LoadStateAdapter<StateAdapter.LoadStateViewHolder>() {

    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): LoadStateViewHolder {

        return LoadStateViewHolder.create(parent, retry)
    }

    class LoadStateViewHolder(private val binding: ItemLoadStateBinding, retry: () -> Unit) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.btnRetry.setOnClickListener {
                retry.invoke()
            }
        }

        fun bind(loadState: LoadState) {

            if (loadState is LoadState.Error) {
                binding.tvError.text = loadState.error.localizedMessage
            }
            binding.progressBar.visibleWhen(loadState is LoadState.Loading)
            binding.tvError.visibleWhen(loadState is LoadState.Error)
            binding.btnRetry.visibleWhen(loadState is LoadState.Error)
        }

        companion object {
            fun create(parent: ViewGroup, retry: () -> Unit): LoadStateViewHolder {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_load_state, parent, false)
                val binding = ItemLoadStateBinding.bind(view)
                return LoadStateViewHolder(binding, retry)
            }
        }
    }
}