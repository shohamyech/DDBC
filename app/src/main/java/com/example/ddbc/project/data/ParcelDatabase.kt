package com.example.ddbc.project.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Parcel::class], version = 1)
abstract class ParcelDatabase : RoomDatabase(){
    abstract fun parcelDao(): ParcelDao
}