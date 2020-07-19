package com.lifestrim.alarmdiary.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.lifestrim.alarmdiary.db.entity.Category
import com.lifestrim.alarmdiary.db.entity.Note

@Dao
interface CategoryDao {
    @Query("SELECT * from category_table")
    fun getAllCategories(): LiveData<List<Category>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(category: Category)

    @Query("DELETE FROM category_table")
    suspend fun deleteAllCategories()

    @Query("DELETE FROM category_table WHERE id = :id")
    suspend fun deleteCategory(id: Int)

}