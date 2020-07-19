package com.lifestrim.alarmdiary.db.repository

import androidx.lifecycle.LiveData
import com.lifestrim.alarmdiary.db.dao.CategoryDao
import com.lifestrim.alarmdiary.db.entity.Category

class CategoryRepository (private val categoryDao: CategoryDao) {
    val allCategories: LiveData<List<Category>> = categoryDao.getAllCategories()

    suspend fun insertCategory(category: Category) {
        categoryDao.insertCategory(category)
    }

    suspend fun deleteCategory(id: Int) {
        categoryDao.deleteCategory(id)
    }

}