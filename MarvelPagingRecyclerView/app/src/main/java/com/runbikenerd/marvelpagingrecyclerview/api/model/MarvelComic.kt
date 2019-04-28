package com.runbikenerd.marvelpagingrecyclerview.api.model

data class MarvelComic(
    val id: Int,
    val digitalId: Int,
    val title: String,
    val description: String,
    val resourceURI: String,
    val thumbnail: Image,
    val images: List<Image>
)