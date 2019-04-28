package com.runbikenerd.marvelpagingrecyclerview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.paging.PagedList
import com.runbikenerd.marvelpagingrecyclerview.api.model.MarvelComic
import com.runbikenerd.marvelpagingrecyclerview.ui.ComicPagedListAdapter
import com.runbikenerd.marvelpagingrecyclerview.viewmodel.ComicsViewModel
import kotlinx.android.synthetic.main.activity_comics_list.*
import org.koin.android.viewmodel.ext.android.viewModel

class ComicsListActivity : AppCompatActivity() {

    private val viewModel: ComicsViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comics_list)
        val adapter = ComicPagedListAdapter {
            viewModel.retry()
        }
        comicsListRecyclerView.adapter = adapter
        viewModel.comics.observe(this, Observer<PagedList<MarvelComic>> {
            adapter.submitList(it)
        })
        viewModel.networkState.observe(this, Observer { adapter.setNetworkState(it) })
    }
}
