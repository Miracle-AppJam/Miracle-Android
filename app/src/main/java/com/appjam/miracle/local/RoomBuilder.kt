package com.appjam.miracle.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit


@Database(entities = [DrawEntity::class], version = 2)
@TypeConverters(RoomTypeConverter::class)
abstract class MiracleDataBase: RoomDatabase() {
    abstract fun drawDao(): DrawDao

    companion object {
        private var instance: MiracleDataBase? = null

        @Synchronized
        fun getInstance(context: Context): MiracleDataBase? {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    MiracleDataBase::class.java, "miracle_database"
                ).build()
            }
            return instance
        }
    }

}