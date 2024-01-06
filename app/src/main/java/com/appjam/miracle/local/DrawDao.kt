package com.appjam.miracle.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface DrawDao {
    @Query("SELECT * FROM draw_table")
    fun getMembers(): List<DrawEntity>

    @Query("SELECT * FROM draw_table WHERE id = :drawId")
    fun getMember(drawId: Int): DrawEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMember(entity: DrawEntity)

    @Update
    fun updateMember(entity: DrawEntity)

    @Delete
    fun deleteMember(entity: DrawEntity)
}