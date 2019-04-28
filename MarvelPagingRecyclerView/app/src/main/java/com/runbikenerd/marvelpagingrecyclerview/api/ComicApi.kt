package com.runbikenerd.marvelpagingrecyclerview.api

import android.util.Log
import com.runbikenerd.marvelpagingrecyclerview.BuildConfig
import com.runbikenerd.marvelpagingrecyclerview.api.model.MarvelComic
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface ComicApi {

    @GET("/v1/public/comics")
    fun getComics(@Query("limit") limit: Int = 30, @Query("offset") offset: Int = 0): Call<ListingResponse>

    data class ListingResponse(
        val code: Int,
        val status: String,
        val etag: String?,
        val data: ListingData?)


    data class ListingData(
        val offset: Int,
        val limit: Int,
        val total: Int,
        val count: Int,
        val results: List<MarvelComic>
    )

    companion object {
        private const val BASE_URL = "https://gateway.marvel.com/"

        fun create(): ComicApi = create(HttpUrl.parse(BASE_URL)!!)

        fun create(httpUrl: HttpUrl): ComicApi {
            val logger = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger {
                Log.d("API", it)
            })
            logger.level = HttpLoggingInterceptor.Level.BASIC

            val apiKeyInterceptor = Interceptor { chain ->
                var request = chain.request()
                val url = request.url().newBuilder()
                    .addQueryParameter("ts", BuildConfig.TimeStamp)
                    .addQueryParameter("apikey", BuildConfig.PublicKey)
                    .addQueryParameter("hash", BuildConfig.Hash)
                    .build()
                request = request.newBuilder()
                    .url(url)
                    .build()
                chain.proceed(request)
            }

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .addInterceptor(apiKeyInterceptor)
                .build()
            return Retrofit.Builder()
                .baseUrl(httpUrl)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ComicApi::class.java)
        }

    }
}