package com.runbikenerd.marvelpagingrecyclerview.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.runbikenerd.marvelpagingrecyclerview.R
import com.runbikenerd.marvelpagingrecyclerview.repository.NetworkState

class NetworkStateItemViewHolder(view: View, retryCallback: () -> Unit): RecyclerView.ViewHolder(view) {

    fun bindTo(networkState: NetworkState?) {

    }

    companion object {
        fun create(parent: ViewGroup, retryCallback: () -> Unit): NetworkStateItemViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.network_state_item, parent, false)
            return NetworkStateItemViewHolder(view, retryCallback)
        }
    }
}