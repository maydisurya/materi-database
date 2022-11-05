package com.chirikualii.materidb.data.local

import android.content.Context
import androidx.room.Room
import com.chirikualii.materidb.data.local.entity.MovieDb

class MovieDbImpl(val context: Context) {
    val db= Room.databaseBuilder(
        context,MovieDb::class.java,"movie.db"
    ).build()

    fun getDatabase():MovieDb{
        return db

    }
}