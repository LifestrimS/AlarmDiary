package com.lifestrim.alarmdiary.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.lifestrim.alarmdiary.db.entity.Note

@Dao
interface NoteDao {
    @Query("SELECT * from note_table")
    fun getAllNotes(): LiveData<List<Note>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: Note)

    @Query("DELETE FROM note_table")
    suspend fun deleteAllNotes()

    @Delete
    suspend fun deleteNote(note: Note)

    @Update
    suspend fun updateNote(note: Note)

    /*@Query("SELECT * FROM note_table WHERE categorycategory_name =:categoryName")
    suspend fun getNoteByCategory(categoryName: String)*/
}