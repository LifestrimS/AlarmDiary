package com.lifestrim.alarmdiary.db.entity

import androidx.room.*
import androidx.room.ForeignKey.*
import com.lifestrim.alarmdiary.util.DateTypeConverter
import java.util.*

@Entity (tableName = "note_table")
data class Note (
    @ColumnInfo(name = "title")
    val noteTitle: String,
    @ColumnInfo(name = "text")
    val noteText: String,
    @ColumnInfo(name = "createDate")
    val noteCreateDate: Date,
    @Embedded (prefix = "category")
    val category: Category

) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}