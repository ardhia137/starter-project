package com.dicoding.asclepius.data.database

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class History(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0,
    @ColumnInfo(name = "image")
    var image: String? = null,
    @ColumnInfo(name = "category")
    var category: String? = null,
    @ColumnInfo(name = "score")
    var score: Float? = null,
):Parcelable
