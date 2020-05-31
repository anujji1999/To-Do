package com.anuj.to_do.clickListeners

import com.anuj.to_do.db.Note

interface ItemClickListener {
    fun onCheck(note : Note)
    fun onDelete(note : Note)
    fun onUndo(note : Note)
}