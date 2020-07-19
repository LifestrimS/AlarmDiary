package com.lifestrim.alarmdiary.db.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.lifestrim.alarmdiary.db.dao.CategoryDao
import com.lifestrim.alarmdiary.db.dao.NoteDao
import com.lifestrim.alarmdiary.db.entity.Category
import com.lifestrim.alarmdiary.db.entity.Note
import com.lifestrim.alarmdiary.util.DateTypeConverter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [Note::class, Category::class], version = 1, exportSchema = false)
@TypeConverters(DateTypeConverter::class)
abstract class NoteRoomDatabase : RoomDatabase() {

    abstract fun noteDao(): NoteDao
    abstract fun categoryDao(): CategoryDao

    companion object {
        @Volatile
        private var INSTANCE: NoteRoomDatabase? = null

        fun getDatabase(context: Context,
                        scope: CoroutineScope
        ): NoteRoomDatabase {
            val tempInstance =
                INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NoteRoomDatabase::class.java,
                    "note_database"
                )
                    .build()
                INSTANCE = instance
                return instance
            }

        }

    }
}

