package com.dkmarkell.rakutentakehome.photo

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

class PhotosResponse(
    @Json(name = "photos") val data: PhotosList
)

class PhotosList(
    @Json(name = "photo") val photos: List<PhotoItem>,
    val page: Int,
    val pages: Int,
    val perPage: Int,
    val total: Int,
)

@Parcelize
data class PhotoItem(
    val id: String,
    val owner: String,
    val secret: String,
    val server: String,
    val farm: Int,
    val title: String,
    @Json(name = "ispublic") val isPublic: Int,
    @Json(name = "isfriend") val isFriend: Int,
    @Json(name = "isfamily") val isFamily: Int,
) : Parcelable

