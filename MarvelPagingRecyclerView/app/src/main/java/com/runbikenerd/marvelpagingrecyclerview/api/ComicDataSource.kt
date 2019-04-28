package com.runbikenerd.marvelpagingrecyclerview.api

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.runbikenerd.marvelpagingrecyclerview.api.model.MarvelComic
import com.runbikenerd.marvelpagingrecyclerview.repository.NetworkState
import retrofit2.Call
import retrofit2.Response
import java.io.IOException
import java.util.concurrent.Executor

class ComicDataSource(private val comicApi: ComicApi, private val retryExecutor: Executor): PageKeyedDataSource<Int, MarvelComic>() {
    private var retry: (() -> Any)? = null

    val networkState = MutableLiveData<NetworkState>()

    val initialLoad = MutableLiveData<NetworkState>()

    fun retryAllFailed() {
        val prevRetry = retry
        retry = null
        prevRetry?.let {
            retryExecutor.execute {
                it.invoke()
            }
        }
    }

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, MarvelComic>) {
        val request = comicApi.getComics(
            limit = params.requestedLoadSize
        )
        networkState.postValue(NetworkState.LOADING)
        initialLoad.postValue(NetworkState.LOADING)

        try {
            val response = request.execute()
            val data = response.body()?.data
            val items = data?.results ?: emptyList()
            retry = null
            networkState.postValue(NetworkState.LOADED)
            initialLoad.postValue(NetworkState.LOADED)
            val nextPageOffset = data?.let {
                if(it.count == it.limit) {
                    it.limit
                } else {
                    null
                }
            }
            callback.onResult(items, 0, nextPageOffset)
        } catch (ioException: IOException) {
            retry = {
                loadInitial(params, callback)
            }
            val error = NetworkState.error(ioException.message ?: "unknown error")
            networkState.postValue(error)
            initialLoad.postValue(error)
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, MarvelComic>) {
        networkState.postValue(NetworkState.LOADING)
        comicApi.getComics(
            offset = params.key,
            limit = params.requestedLoadSize).enqueue(
            object : retrofit2.Callback<ComicApi.ListingResponse> {
                override fun onFailure(call: Call<ComicApi.ListingResponse>, t: Throwable) {
                    retry = {
                        loadAfter(params, callback)
                    }
                    networkState.postValue(NetworkState.error(t.message ?: "unknown err"))
                }

                override fun onResponse(
                    call: Call<ComicApi.ListingResponse>,
                    response: Response<ComicApi.ListingResponse>
                ) {
                    if (response.isSuccessful) {
                        val data = response.body()?.data
                        val items = data?.results ?: emptyList()
                        retry = null
                        val nextPageOffset = data?.let {
                            if(it.count == it.limit) {
                                it.offset + it.limit
                            } else {
                                null
                            }
                        }
                        callback.onResult(items, nextPageOffset)
                        networkState.postValue(NetworkState.LOADED)
                    } else {
                        retry = {
                            loadAfter(params, callback)
                        }
                        networkState.postValue(
                            NetworkState.error("error code: ${response.code()}"))
                    }
                }
            }
        )
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, MarvelComic>) {}
}