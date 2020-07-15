package com.lifestrim.alarmdiary.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.lifestrim.alarmdiary.repository.NoteRepository
import com.lifestrim.alarmdiary.db.NoteRoomDatabase
import com.lifestrim.alarmdiary.db.entity.Note
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NoteViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: NoteRepository
    val allWords: LiveData<List<Note>>

    init {
        val wordsDao = NoteRoomDatabase.getDatabase(
            application,
            viewModelScope
        ).noteDao()
        repository = NoteRepository(wordsDao)
        allWords = repository.allWords
    }

    fun update(note: Note) = viewModelScope.launch(Dispatchers.IO) {
        repository.update(note)
    }

    fun insert(note: Note) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(note)
    }

    fun delete(note: Note) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(note)
    }
}