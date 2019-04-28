package com.runbikenerd.marvelpagingrecyclerview.ui

import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.runbikenerd.marvelpagingrecyclerview.R
import com.runbikenerd.marvelpagingrecyclerview.api.model.MarvelComic
import com.runbikenerd.marvelpagingrecyclerview.repository.NetworkState

class ComicPagedListAdapter(private val retryCallback: () -> Unit): PagedListAdapter<MarvelComic, RecyclerView.ViewHolder>(COMIC_COMPARATOR) {
    private var networkState: NetworkState? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.comic_item -> ComicItemViewHolder.create(parent)
            R.layout.network_state_item -> NetworkStateItemViewHolder.create(parent, retryCallback)
            else -> throw IllegalArgumentException("unknown view type $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            R.layout.comic_item -> (holder as ComicItemViewHolder).bindTo(getItem(position))
            R.layout.network_state_item -> (holder as NetworkStateItemViewHolder).bindTo(networkState)
        }
    }

    private fun hasExtraRow() = networkState != null && networkState != NetworkState.LOADED

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1) {
            R.layout.network_state_item
        } else {
            R.layout.comic_item
        }
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasExtraRow()) 1 else 0
    }

    fun setNetworkState(newNetworkState: NetworkState?) {
        val previousState = this.networkState
        val hadExtraRow = hasExtraRow()
        this.networkState = newNetworkState
        val hasExtraRow = hasExtraRow()
        if (hadExtraRow != hasExtraRow) {
            if (hadExtraRow) {
                notifyItemRemoved(super.getItemCount())
            } else {
                notifyItemInserted(super.getItemCount())
            }
        } else if (hasExtraRow && previousState != newNetworkState) {
            notifyItemChanged(itemCount - 1)
        }
    }

    companion object {
        val COMIC_COMPARATOR = object : DiffUtil.ItemCallback<MarvelComic>() {
            override fun areItemsTheSame(oldItem: MarvelComic, newItem: MarvelComic): Boolean = oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: MarvelComic, newItem: MarvelComic): Boolean = oldItem == newItem
        }
    }
}