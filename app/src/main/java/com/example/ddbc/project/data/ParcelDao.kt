package com.example.ddbc.project.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ParcelDao {

    @Query("SELECT * FROM parcels")
    suspend fun getAll(): List<Parcel>

    @Insert
    suspend fun insert(parcel: Parcel)
}