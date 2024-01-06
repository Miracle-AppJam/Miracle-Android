package com.appjam.miracle.local

import android.graphics.Bitmap
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "draw_table")
data class DrawEntity (
    @PrimaryKey()
    val id: Int = 0,

    @ColumnInfo(name = "image")
    val image: Bitmap,

    @ColumnInfo(name = "message")
    val message: String,
)