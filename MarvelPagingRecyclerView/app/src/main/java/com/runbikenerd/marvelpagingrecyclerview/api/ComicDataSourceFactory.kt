package com.runbikenerd.marvelpagingrecyclerview.api

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.runbikenerd.marvelpagingrecyclerview.api.model.MarvelComic
import java.util.concurrent.Executor

class ComicDataSourceFactory(private val comicApi: ComicApi, private val retryExecutor: Executor): DataSource.Factory<Int, MarvelComic>() {
    val sourceLiveData = MutableLiveData<ComicDataSource>()
    var latestSource: ComicDataSource? = null
    override fun create(): DataSource<Int, MarvelComic> {
        latestSource = ComicDataSource(comicApi, retryExecutor)
        sourceLiveData.postValue(latestSource)
        return latestSource as DataSource<Int, MarvelComic>
    }

}