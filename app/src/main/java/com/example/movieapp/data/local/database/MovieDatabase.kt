package com.example.movieapp.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.movieapp.data.local.dao.MovieDao
import com.example.movieapp.data.local.entity.MovieDetailEntity
import com.example.movieapp.data.local.entity.MovieEntity

@Database(
    entities = [
        MovieEntity::class,
        MovieDetailEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class MovieDatabase : RoomDatabase() {

    abstract fun movieDao(): MovieDao

    companion object {
        const val DATABASE_NAME = "movie_database"
    }
}