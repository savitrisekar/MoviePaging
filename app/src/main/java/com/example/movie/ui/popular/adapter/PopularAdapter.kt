package com.example.movie.ui.popular.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.movie.data.model.PopularModel
import com.example.movie.databinding.ItemPopularBinding
import com.example.movie.ui.popular.PopularViewModel

class PopularAdapter(private val viewModel: PopularViewModel) :
    PagingDataAdapter<PopularModel, PopularAdapter.ViewHolder>(diffUtilCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(this.viewModel, item)
        }
    }

    class ViewHolder(private val binding: ItemPopularBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(viewModel: PopularViewModel, model: PopularModel) {

            binding.movie = model
            binding.viewModel = viewModel
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemPopularBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

    companion object {

        private val diffUtilCallback = object :
            DiffUtil.ItemCallback<PopularModel>() {
            override fun areItemsTheSame(
                oldItem: PopularModel,
                newItem: PopularModel
            ): Boolean {
                return oldItem.title == newItem.title
            }

            override fun areContentsTheSame(
                oldItem: PopularModel,
                newItem: PopularModel
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}