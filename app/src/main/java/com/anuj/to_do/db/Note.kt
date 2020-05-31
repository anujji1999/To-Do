package com.anuj.to_do.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo

@Entity(tableName = "notesDao")
data class Note (
    @PrimaryKey(autoGenerate = true)
    var id : Int ?= null,

    @ColumnInfo(name = "description")
    var description : String = "",

    @ColumnInfo(name = "isPriority")
    var isPriority : Boolean = false,

    @ColumnInfo(name = "isTaskCompleted")
    var isTaskCompleted : Boolean = false
)