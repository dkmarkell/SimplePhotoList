package com.dkmarkell.rakutentakehome.photo

import com.dkmarkell.rakutentakehome.photo.details.PhotoDetails
import com.dkmarkell.rakutentakehome.photo.list.PhotoPreview

object TypeConverters {
    fun PhotoItem.asPhotoEntity() = PhotoEntity(
        remoteId = id,
        owner = owner,
        secret = secret,
        server = server,
        farm = farm,
        title = title,
        isFamily = isFamily,
        isPublic = isPublic,
        isFriend = isFriend
    )

    fun List<PhotoItem>.asPhotoEntityList() = map {
        it.asPhotoEntity()
    }

    fun PhotosResponse.asPhotoEntityList() = data.photos.asPhotoEntityList()

    fun PhotoEntity.asPhotoPreview(): PhotoPreview {
        val url = UrlBuilder.build(server, remoteId, secret, UrlBuilder.PhotoSize.THUMBNAIL_150)

        return PhotoPreview(id = localId, title = title, url = url)
    }

    fun List<PhotoEntity>.asPhotoPreviewList() = map {
        it.asPhotoPreview()
    }

    fun PhotoEntity.asPhotoDetails(): PhotoDetails {
        val url = UrlBuilder.build(server, remoteId, secret, UrlBuilder.PhotoSize.LARGE_1024)

        return PhotoDetails(
            localId = localId,
            remoteId = remoteId,
            owner = owner,
            secret = secret,
            server = server,
            farm = farm,
            title = title,
            isPublic = isPublic != 0,
            isFriend = isFriend != 0,
            isFamily = isFamily != 0,
            url = url
        )
    }
}