package com.a90ms.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.a90ms.data.db.search.MovieDao
import com.a90ms.domain.db.LocalMovieEntity

@Database(
    entities = [
        LocalMovieEntity::class
    ],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun movieDao(): MovieDao

    suspend fun clearDB() {
        movieDao().clear()
    }

    companion object {
        private const val DB_NAME = "sample-db"

        fun buildDatabase(context: Context): AppDatabase =
            Room.databaseBuilder(context, AppDatabase::class.java, DB_NAME)
                .build()
    }
}
