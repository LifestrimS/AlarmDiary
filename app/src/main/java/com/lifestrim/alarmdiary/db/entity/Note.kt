package com.lifestrim.alarmdiary.db.entity

import androidx.room.*
import java.util.*

@Entity
data class Note (
    @PrimaryKey val noteId: Int,
    @ColumnInfo(name = "noteTitle") val noteTitle: String
    /*@ColumnInfo(name = "noteText") val noteText: String,
    @ColumnInfo(name = "noteDateCreate")  val noteDateCreate: Date?,
    @ColumnInfo(name = "noteDateChanged") val noteDateChanged: Date?*/
)