package com.dkmarkell.rakutentakehome.photo.details

data class PhotoDetails(
    val localId: Long = 0L,
    val remoteId: String,
    val owner: String,
    val server: String,
    val secret: String,
    val farm: Int,
    val title: String,
    val isPublic: Boolean,
    val isFriend: Boolean,
    val isFamily: Boolean,
    val url: String
)
