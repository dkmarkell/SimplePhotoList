package com.dkmarkell.rakutentakehome.photo

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PhotoDao {

    @Insert
    suspend fun insertPhotos(photos: List<PhotoEntity>): List<Long>

    @Query ("SELECT * FROM photos WHERE local_id = :id LIMIT 1")
    suspend fun getPhotoById (id: Long) : PhotoEntity?

    @Query("SELECT * FROM photos")
    fun observeAllPhotos(): Flow<List<PhotoEntity>>

    @Query("DELETE FROM photos")
    suspend fun clearPhotos()
}