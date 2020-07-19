package com.lifestrim.alarmdiary.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.lifestrim.alarmdiary.db.repository.NoteRepository
import com.lifestrim.alarmdiary.db.database.NoteRoomDatabase
import com.lifestrim.alarmdiary.db.entity.Note
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NoteViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: NoteRepository
    val allNotes: LiveData<List<Note>>

    init {
        val noteDao = NoteRoomDatabase.getDatabase(
            application,
            viewModelScope
        ).noteDao()
        repository = NoteRepository(noteDao)
        allNotes = repository.allNotes
    }

    fun updateNote(note: Note) = viewModelScope.launch(Dispatchers.IO) {
        repository.updateNote(note)
    }

    fun insertNote(note: Note) = viewModelScope.launch(Dispatchers.IO) {
        repository.insertNote(note)
    }

    fun deleteNote(note: Note) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteNote(note)
    }
}