package com.dkmarkell.rakutentakehome.photo

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "photos")
data class PhotoEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "local_id") val localId: Long = 0L,
    @ColumnInfo(name = "remote_id") val remoteId: String,
    val owner: String,
    val secret: String,
    val server: String,
    val farm: Int,
    val title: String,
    @ColumnInfo(name = "is_public")val isPublic: Int,
    @ColumnInfo(name = "is_friend")val isFriend: Int,
    @ColumnInfo(name = "is_family")val isFamily: Int,
)
