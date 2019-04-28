package com.runbikenerd.marvelpagingrecyclerview.repository

import androidx.lifecycle.Transformations
import androidx.paging.toLiveData
import com.runbikenerd.marvelpagingrecyclerview.api.ComicApi
import com.runbikenerd.marvelpagingrecyclerview.api.ComicDataSourceFactory
import com.runbikenerd.marvelpagingrecyclerview.api.model.MarvelComic
import java.util.concurrent.Executor

class MarvelComicsRepository(private val comicApi: ComicApi, private val networkExecutor: Executor) {
    fun listOfComics(pageSize: Int): Listing<MarvelComic> {
        val sourceFactory = ComicDataSourceFactory(comicApi, networkExecutor)

        val livePagedList = sourceFactory.toLiveData(
            pageSize = pageSize,
            fetchExecutor = networkExecutor)

        val refreshState = Transformations.switchMap(sourceFactory.sourceLiveData) {
            it.initialLoad
        }
        return Listing(
            pagedList = livePagedList,
            networkState = Transformations.switchMap(sourceFactory.sourceLiveData) {
                it.networkState
            },
            retry = {
                sourceFactory.sourceLiveData.value?.retryAllFailed()
            },
            refresh = {
                sourceFactory.sourceLiveData.value?.invalidate()
            },
            refreshState = refreshState
        )
    }

}