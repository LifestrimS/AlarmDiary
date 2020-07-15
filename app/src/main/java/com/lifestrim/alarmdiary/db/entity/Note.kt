package com.lifestrim.alarmdiary.db.entity

import androidx.room.*
import com.lifestrim.alarmdiary.util.DateTypeConverter
import java.util.*

@Entity (tableName = "note_table")
data class Note (
    @ColumnInfo(name = "title")
    val noteTitle: String,
    @ColumnInfo(name = "text")
    val noteText: String,
    @ColumnInfo(name = "createDate")
    val noteCreateDate: Date

) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}