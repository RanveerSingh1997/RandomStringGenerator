package com.example.randomstring.database

import androidx.room.Database
import androidx.room.RoomDatabase

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Delete

@Database(entities = [StringEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun stringDao(): StringDao
}


@Dao
interface StringDao {
    @Insert
    suspend fun insert(string: StringEntity)

    @Query("SELECT * FROM random_strings ORDER BY id DESC")
    suspend fun getAllStrings(): List<StringEntity>

    @Query("DELETE FROM random_strings")
    suspend fun deleteAll()

    @Delete
    suspend fun deleteString(string: StringEntity)
}