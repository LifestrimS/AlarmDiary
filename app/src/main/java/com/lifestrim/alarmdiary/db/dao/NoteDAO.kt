package com.lifestrim.alarmdiary.db.dao

import androidx.room.*
import com.lifestrim.alarmdiary.db.entity.Note

@Dao
interface NoteDAO {
    @Query("SELECT * FROM note")
    fun getAllNote(): List<Note>

    @Query("SELECT * FROM note WHERE noteId ==:noteId")
    fun getNoteById(noteId: Int): List<Note>

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    fun insertNote(note: Note)

    @Delete
    fun deleteNote(note: Note)

    @Update
    fun updateNote(note: Note)
}