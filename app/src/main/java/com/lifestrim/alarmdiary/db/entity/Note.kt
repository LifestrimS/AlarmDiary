package com.lifestrim.alarmdiary.db.entity

import androidx.room.*
import java.util.*

@SuppressWarnings(RoomWarnings.PRIMARY_KEY_FROM_EMBEDDED_IS_DROPPED)
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