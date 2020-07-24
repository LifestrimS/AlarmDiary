package com.lifestrim.alarmdiary.db.repository

import androidx.lifecycle.LiveData
import com.lifestrim.alarmdiary.db.dao.NoteDao
import com.lifestrim.alarmdiary.db.entity.Note

class NoteRepository(private val noteDao: NoteDao) {
    val allNotes: LiveData<List<Note>> = noteDao.getAllNotes()

    suspend fun insertNote(note: Note) {
        noteDao.insertNote(note)
    }

    suspend fun deleteNote(note: Note) {
        noteDao.deleteNote(note)
    }

    suspend fun updateNote(note: Note) {
        noteDao.updateNote(note)
    }

    /*suspend fun getNoteByCategory(categoryName: String) {
        noteDao.getNoteByCategory(categoryName)
    }*/
}