package com.dkmarkell.rakutentakehome.photo

object UrlBuilder {

    private const val BASE_PHOTO_URL = "https://live.staticflickr.com"

    enum class PhotoSize(val suffix: String) {
        THUMBNAIL_150("q"),
        THUMBNAIL_100("t"),
        LARGE_1024("b"),
        EXTRA_LARGE_4K("4k"),
        ORIGINAL("o"),
    }

    fun build(
        server: String,
        id: String,
        secret: String,
        size: PhotoSize,
    ) = "$BASE_PHOTO_URL/$server/${id}_${secret}_${size.suffix}.jpg"

}