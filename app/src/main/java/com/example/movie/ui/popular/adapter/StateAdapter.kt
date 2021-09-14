package com.example.movie.ui.popular.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.movie.R
import com.example.movie.visibleWhen
import kotlinx.android.synthetic.main.item_load_state.view.*

class StateAdapter(private val retry: () -> Unit) :
    LoadStateAdapter<StateAdapter.LoadStateViewHolder>() {
    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): LoadStateViewHolder {
        return LoadStateViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_load_state, parent, false),
            retry
        )
    }

    inner class LoadStateViewHolder(view: View, retry: () -> Unit) : RecyclerView.ViewHolder(view) {
        init {
            itemView.btn_retry.setOnClickListener {
                retry.invoke()
            }
        }

        fun bind(loadState: LoadState) {
            itemView.apply {
                progressBar.visibleWhen(loadState is LoadState.Loading)
                tv_error.visibleWhen(loadState is LoadState.Error)
                btn_retry.visibleWhen(loadState is LoadState.Error)
            }

            if (loadState is LoadState.Error) {
                itemView.tv_error.text = loadState.error.localizedMessage
            }
        }
    }
}