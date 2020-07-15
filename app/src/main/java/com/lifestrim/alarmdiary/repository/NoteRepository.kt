package com.lifestrim.alarmdiary.repository

import androidx.lifecycle.LiveData
import com.lifestrim.alarmdiary.db.dao.NoteDao
import com.lifestrim.alarmdiary.db.entity.Note

class NoteRepository(private val noteDao: NoteDao) {
    val allWords: LiveData<List<Note>> = noteDao.getAllNotes()

    suspend fun insert(note: Note) {
        noteDao.insert(note)
    }

    suspend fun delete(note: Note) {
        noteDao.deleteNote(note)
    }

    suspend fun update(note: Note) {
        noteDao.updateNote(note)
    }
}