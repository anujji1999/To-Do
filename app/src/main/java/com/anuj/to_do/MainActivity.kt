package com.anuj.to_do

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.anuj.to_do.Adapters.NotesAdapter
import com.anuj.to_do.clickListeners.ItemClickListener
import com.anuj.to_do.db.Note
import com.anuj.to_do.db.NotesDatabase
import kotlinx.android.synthetic.main.add_note_dialog.*

class MainActivity : AppCompatActivity() {

    lateinit var recyclerViewNotes : RecyclerView
    var notesList = ArrayList<Note>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bindViews()
        getDataFromDatabase()
        setupRecyclerView()
    }

    private fun getDataFromDatabase() {
        val notesDao = NotesDatabase.getInstance(this).notesDao()
        notesList.addAll(notesDao.getAllPriority())
        notesList.addAll(notesDao.getAllNonPriority())
    }

    private fun bindViews() {
        recyclerViewNotes = findViewById(R.id.recyclerViewNotes)
    }

    private fun setupRecyclerView() {
        val itemclickListener = object : ItemClickListener {

            val notesDao = NotesDatabase.getInstance(this@MainActivity).notesDao()

            override fun onCheck(note: Note) {
                notesDao.updateNote(note)
                setupRecyclerView()
            }

            override fun onDelete(note: Note) {
                notesDao.delete(note)
                notesList.clear()
                getDataFromDatabase()
                setupRecyclerView()
            }

            override fun onUndo(note: Note) {
                notesDao.updateNote(note)
                setupRecyclerView()
            }
        }

        val notesAdapter = NotesAdapter(notesList, itemclickListener)
        val linearLayoutManager = LinearLayoutManager(this@MainActivity)
        linearLayoutManager.orientation = RecyclerView.VERTICAL
        recyclerViewNotes.layoutManager = linearLayoutManager
        recyclerViewNotes.adapter = notesAdapter
        notesAdapter.notifyDataSetChanged()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)

        val search = menu?.findItem(R.id.search)
        val searchView = search?.actionView as androidx.appcompat.widget.SearchView
        searchView.isSubmitButtonEnabled = true
        searchView.queryHint = "Type something..."
        searchView.setOnQueryTextListener(object: androidx.appcompat.widget.SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    getItemsFromDb(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    getItemsFromDb(newText)
                }
                return true
            }
        })
        return true
    }

    private fun getItemsFromDb(searchText: String) {
        var searchText = searchText
        searchText = "%$searchText%"

        val notesDao = NotesDatabase.getInstance(this).notesDao()
        notesList.clear()
        notesList.addAll(notesDao.getSearchResults(searchText))
        setupRecyclerView()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.addNote -> {
                setupDialogBox()
                return true
            }
            R.id.search -> {
                return true
            }
            else -> {
                return super.onOptionsItemSelected(item)
            }
        }
    }

    private fun setupDialogBox() {
        val view = LayoutInflater.from(this).inflate(R.layout.add_note_dialog, null)
        val dialog = AlertDialog.Builder(this)
            .setView(view)
            .setCancelable(false)
            .create()

        val editTextDescription : EditText = view.findViewById(R.id.editTextDescription)
        val checkboxIsPriority : CheckBox = view.findViewById(R.id.checkboxIsPriority)
        val textViewIsPriority : TextView = view.findViewById(R.id.textViewIsPriority)
        val buttonCancel : Button = view.findViewById(R.id.buttonCancel)
        val buttonAddNote : Button = view.findViewById(R.id.buttonAddNote)

        textViewIsPriority.setOnClickListener(View.OnClickListener {
            checkboxIsPriority.toggle()
        })

        buttonCancel.setOnClickListener(View.OnClickListener {
            dialog.hide()
        })

        buttonAddNote.setOnClickListener(View.OnClickListener {
            val description = editTextDescription.text.toString()
            val note = Note(description = description, isPriority = checkboxIsPriority.isChecked)
            if(description.isNotEmpty()){
                notesList.add(note)
                addNotesToDb(note)
            }else{
                Toast.makeText(this@MainActivity, "Task can't be empty!", Toast.LENGTH_SHORT).show()
            }
            setupRecyclerView()
            dialog.hide()
        })
        dialog.show()
    }

    private fun addNotesToDb(notes: Note) {
        val notesDao = NotesDatabase.getInstance(this).notesDao()
        notesDao.insert(notes)
        notesList.clear()
        getDataFromDatabase()
        setupRecyclerView()
    }
}
