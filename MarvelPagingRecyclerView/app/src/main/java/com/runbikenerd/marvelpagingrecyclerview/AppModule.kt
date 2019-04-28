package com.runbikenerd.marvelpagingrecyclerview

import com.runbikenerd.marvelpagingrecyclerview.api.ComicApi
import com.runbikenerd.marvelpagingrecyclerview.repository.MarvelComicsRepository
import com.runbikenerd.marvelpagingrecyclerview.viewmodel.ComicsViewModel
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module
import java.util.concurrent.Executors

private val NETWORK_IO = Executors.newFixedThreadPool(5)

val appModule = module {
    single { MarvelComicsRepository(ComicApi.create(), NETWORK_IO)}
    viewModel { ComicsViewModel(get()) }
}