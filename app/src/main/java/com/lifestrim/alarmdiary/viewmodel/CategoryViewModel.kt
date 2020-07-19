package com.lifestrim.alarmdiary.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.lifestrim.alarmdiary.db.database.NoteRoomDatabase
import com.lifestrim.alarmdiary.db.entity.Category
import com.lifestrim.alarmdiary.db.repository.CategoryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CategoryViewModel (application: Application) : AndroidViewModel(application) {
    private val repository: CategoryRepository
    val allCategories: LiveData<List<Category>>

    init {
        val categoryDao = NoteRoomDatabase.getDatabase(
            application,
            viewModelScope
        ).categoryDao()
        repository = CategoryRepository(categoryDao)
        allCategories = repository.allCategories
    }

    fun categoryInsert(category: Category) = viewModelScope.launch(Dispatchers.IO) {
        repository.insertCategory(category)
    }

    fun categoryDelete(id: Int) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteCategory(id)
    }

}


