package com.runbikenerd.marvelpagingrecyclerview.viewmodel

import androidx.lifecycle.ViewModel
import com.runbikenerd.marvelpagingrecyclerview.repository.MarvelComicsRepository

class ComicsViewModel(comicRepository: MarvelComicsRepository): ViewModel() {

    private val repoResult = comicRepository.listOfComics(30)

    val comics = repoResult.pagedList
    val networkState = repoResult.networkState
    val refreshState = repoResult.refreshState

    fun refresh() {
        repoResult.refresh.invoke()
    }

    fun retry() {
        repoResult.retry.invoke()
    }
}