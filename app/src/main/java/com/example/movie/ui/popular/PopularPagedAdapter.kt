package com.example.movie.ui.popular

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movie.R
import com.example.movie.data.api.POSTER_BASE_URL
import com.example.movie.data.model.PopularMovie
import com.example.movie.data.repository.NetworkState
import com.example.movie.ui.details.PopularDetailsActivity
import kotlinx.android.synthetic.main.item_network_state.view.*
import kotlinx.android.synthetic.main.item_popular.view.*

class PopularPagedAdapter(public val context: Context) :
    PagedListAdapter<PopularMovie, RecyclerView.ViewHolder>(UpcomingDiffCallback()) {

    val UPCOMING_VIEW_TYPE = 1 //show movie upcoming
    val NETWORK_VIEW_TYPE = 2 //show network state

    private var networkState: NetworkState? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view: View

        return if (viewType == UPCOMING_VIEW_TYPE) {
            view = layoutInflater.inflate(R.layout.item_popular, parent, false)
            UpcomingViewHolder(view)
        } else {
            view = layoutInflater.inflate(R.layout.item_network_state, parent, false)
            NetworkStateViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == UPCOMING_VIEW_TYPE) {
            (holder as UpcomingViewHolder).bind(getItem(position), context)
        } else {
            (holder as NetworkStateViewHolder).bind(networkState)
        }
    }

    private fun hasExtraRow(): Boolean {
        return networkState != null && networkState != NetworkState.LOADED
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasExtraRow()) 1 else 0
    }

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1) {
            NETWORK_VIEW_TYPE
        } else {
            UPCOMING_VIEW_TYPE
        }
    }

    class UpcomingDiffCallback : DiffUtil.ItemCallback<PopularMovie>() {
        override fun areItemsTheSame(oldItem: PopularMovie, newItem: PopularMovie): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: PopularMovie, newItem: PopularMovie): Boolean {
            return oldItem == newItem
        }
    }

    class UpcomingViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(upcomingMovie: PopularMovie?, context: Context) {
            itemView.tv_title_movie.text = upcomingMovie?.title
            itemView.tv_date.text = upcomingMovie?.releaseDate

            val imgMovie = POSTER_BASE_URL + upcomingMovie?.posterPath
            Glide.with(itemView.context)
                .load(imgMovie)
                .into(itemView.iv_poster_movie)

            itemView.setOnClickListener {
                val intent = Intent(context, PopularDetailsActivity::class.java)
                intent.putExtra("id", upcomingMovie?.id)
                context.startActivity(intent)
            }
        }
    }

    class NetworkStateViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(networkState: NetworkState?) {
            if (networkState != null && networkState == NetworkState.LOADING) {
                itemView.progressBar.visibility = View.VISIBLE;
            } else {
                itemView.progressBar.visibility = View.GONE;
            }

            if (networkState != null && networkState == NetworkState.ERROR) {
                itemView.tv_error.visibility = View.VISIBLE;
                itemView.tv_error.text = networkState.msg;
            } else if (networkState != null && networkState == NetworkState.ENLIST) {
                itemView.tv_error.visibility = View.VISIBLE;
                itemView.tv_error.text = networkState.msg;
            } else {
                itemView.tv_error.visibility = View.GONE;
            }
        }
    }

    fun setNetworkState(newNetworkState: NetworkState) {
        val previousState = this.networkState
        val hadExtraRow = hasExtraRow()
        this.networkState = newNetworkState
        val hasExtraRow = hasExtraRow()

        if (hadExtraRow != hasExtraRow) {
            if (hadExtraRow) {                             //hadExtraRow is true and hasExtraRow false
                notifyItemRemoved(super.getItemCount())    //remove the progressbar at the end
            } else {                                       //hasExtraRow is true and hadExtraRow false
                notifyItemInserted(super.getItemCount())   //add the progressbar at the end
            }
        } else if (hasExtraRow && previousState != newNetworkState) { //hasExtraRow is true and hadExtraRow true and (NetworkState.ERROR or NetworkState.ENDOFLIST)
            notifyItemChanged(itemCount - 1)       //add the network message at the end
        }

    }
}