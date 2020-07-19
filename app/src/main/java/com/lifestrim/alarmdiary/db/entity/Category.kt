package com.lifestrim.alarmdiary.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "category_table")
data class Category (
    @ColumnInfo (name = "category_name")
    val nameCategory: String?,
    @ColumnInfo (name = "category_color")
    val colorCategory: Int?
){
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}