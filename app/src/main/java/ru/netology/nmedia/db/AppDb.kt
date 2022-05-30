package ru.netology.nmedia.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [PostEntity::class],
    version = 1
)
abstract class AppDb : RoomDatabase() {
    abstract val postDao: PostDao

    companion object {
        @Volatile
        private var instance: AppDb? = null

        fun getInstance(context: Context): AppDb {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): AppDb {
            return Room.databaseBuilder(context, AppDb::class.java, "app.db")
                .allowMainThreadQueries()
                .build()
        }
    }
}