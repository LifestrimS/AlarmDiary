package com.lifestrim.alarmdiary.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.lifestrim.alarmdiary.db.entity.Note

@Dao
interface NoteDao {
    @Query("SELECT * from note_table")
    fun getAllNotes(): LiveData<List<Note>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(note: Note)

    @Query("DELETE FROM note_table")
    suspend fun deleteAll()

    @Delete
    fun deleteNote(note: Note)

    @Update
    fun updateNote(note: Note)
}