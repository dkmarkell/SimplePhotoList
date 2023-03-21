package com.dkmarkell.rakutentakehome.photo

import retrofit2.Response
import retrofit2.http.GET

interface PhotoApi {

    @GET("/rakuten-rewards/photos.json?method=flickr.photos.getRecent&api_key=fee10de350d1f31d5fec0eaf330d2dba&page=1&format=json&nojsoncallback=true&safe_search=true")
    suspend fun getPhotos(): Response<PhotosResponse>
}