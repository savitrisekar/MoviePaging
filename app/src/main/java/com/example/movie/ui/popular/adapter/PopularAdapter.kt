package com.example.movie.ui.popular.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movie.R
import com.example.movie.data.model.PopularModel
import kotlinx.android.synthetic.main.item_popular.view.*

class PopularAdapter :
    PagingDataAdapter<PopularModel, PopularAdapter.ViewHolder>(diffUtilCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_popular, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val title: TextView = view.tv_title_movie
        private val date: TextView = view.tv_date
        private var popularModel: PopularModel? = null

        fun bind(popularModel: PopularModel) {

            title.text = popularModel.title
            date.text = popularModel.releaseDate

            val imgMovie = POSTER_BASE_URL + popularModel?.posterPath
            Glide.with(itemView.context)
                .load(imgMovie)
                .into(itemView.iv_poster_movie)

            this.popularModel = popularModel

//            itemView.setOnClickListener {
//                val intent = Intent(context, PopularDetailsActivity::class.java)
//                intent.putExtra("id", upcomingMovie?.id)
//                context.startActivity(intent)
//            }
        }
    }

    companion object {
        private const val POSTER_BASE_URL = "https://image.tmdb.org/t/p/w342/"

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