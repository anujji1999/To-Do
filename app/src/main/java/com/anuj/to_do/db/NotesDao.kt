package com.anuj.to_do.db

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE

@Dao
interface NotesDao{
    @Query("SELECT * from notesDao where isPriority = 1")
    fun getAllPriority() : List<Note>

    @Query("SELECT * from notesDao where isPriority = 0")
    fun getAllNonPriority() : List<Note>

    @Insert(onConflict = REPLACE)
    fun insert(note : Note)

    @Update
    fun updateNote(note : Note)

    @Delete
    fun delete(note : Note)

    @Query("Select * from notesDao where description like  :desc")
    fun getSearchResults(desc : String) : List<Note>
}