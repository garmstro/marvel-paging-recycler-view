package com.runbikenerd.marvelpagingrecyclerview.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.runbikenerd.marvelpagingrecyclerview.R
import com.runbikenerd.marvelpagingrecyclerview.api.model.MarvelComic

class ComicItemViewHolder(view: View): RecyclerView.ViewHolder(view) {
    private val comicTitle: TextView = view.findViewById(R.id.comicTitle)

    fun bindTo(comic: MarvelComic?) {
        comicTitle.text = comic?.title.orEmpty()
    }

    companion object {
        fun create(parent: ViewGroup): ComicItemViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.comic_item, parent, false)
            return ComicItemViewHolder(view)
        }
    }
}