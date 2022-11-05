package com.chirikualii.materidb.data.local.entity

import androidx.room.Database
import androidx.room.RoomDatabase
import com.chirikualii.materidb.data.local.dao.MovieDao

@Database(entities = [MovieEntity::class], version = 1)
abstract class MovieDb : RoomDatabase(){

    abstract fun movieDao():  MovieDao
}