package com.example.mediaplayer.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [AlbumListEntity::class], version = 1, exportSchema = false)
abstract class AlbumListDataBase: RoomDatabase() {
    companion object {
        const val DB_NAME: String = "DB_ALBUM_LIST"
        private var database: AlbumListDataBase? = null
        fun getInstance(context: Context): AlbumListDataBase {
            if (database == null) {
                synchronized(this) {
                    if (database == null) {
                        database = Room.databaseBuilder(context.applicationContext,
                            AlbumListDataBase::class.java, DB_NAME).build()
                    }
                }
            }
            return database!!
        }
    }

    abstract fun albumListDao(): AlbumListDAO
}