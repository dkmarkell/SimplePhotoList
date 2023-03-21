package com.dkmarkell.rakutentakehome.localstorage

import androidx.room.Database
import androidx.room.RoomDatabase
import com.dkmarkell.rakutentakehome.photo.PhotoDao
import com.dkmarkell.rakutentakehome.photo.PhotoEntity

@Database(entities = [PhotoEntity::class], version = 1, exportSchema = false)
abstract class LocalDatabase : RoomDatabase() {
    abstract val photoDao: PhotoDao
}