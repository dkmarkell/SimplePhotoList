package com.dkmarkell.rakutentakehome.photo

import com.dkmarkell.rakutentakehome.photo.TypeConverters.asPhotoDetails
import com.dkmarkell.rakutentakehome.photo.TypeConverters.asPhotoEntityList
import com.dkmarkell.rakutentakehome.photo.TypeConverters.asPhotoPreviewList
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PhotoRepository @Inject constructor(
    private val photoDao: PhotoDao,
    private val photoApi: PhotoApi
) {

    val photoPreviews = photoDao.observeAllPhotos().map { it.asPhotoPreviewList() }

    /**
     * suspend function that gets the list of photos from the photo api.
     *
     * If the call succeeds, insert all items into the local database and
     * return true. If the call fails, return false to notify an error
     * occurred.
     */
    suspend fun getPhotos(): Boolean {
        return try {
            val response = photoApi.getPhotos()
            if (response.isSuccessful) {
                val photoEntities = response.body()?.asPhotoEntityList() ?: emptyList()
                photoDao.insertPhotos(photoEntities)
                true
            } else {
                false
            }
        } catch (e: Exception) {
            false
        }
    }

    suspend fun getPhoto(id: Long) = photoDao.getPhotoById(id)?.asPhotoDetails()

    suspend fun deleteAllPhotos() = photoDao.clearPhotos()
}