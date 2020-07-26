package com.lifestrim.alarmdiary.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.lifestrim.alarmdiary.R
import com.lifestrim.alarmdiary.adapter.NoteListAdapter
import com.lifestrim.alarmdiary.db.entity.Category
import com.lifestrim.alarmdiary.db.entity.Note
import com.lifestrim.alarmdiary.viewmodel.CategoryViewModel
import com.lifestrim.alarmdiary.viewmodel.NoteViewModel
import com.microsoft.appcenter.AppCenter
import com.microsoft.appcenter.analytics.Analytics
import com.microsoft.appcenter.crashes.Crashes
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class MainActivity : AppCompatActivity() {
    companion object {
        const val ADD_NOTE_REQUEST = 1
        const val EDIT_NOTE_REQUEST = 2
    }

    private lateinit var recyclerView : RecyclerView
    val adapter = NoteListAdapter()
    private lateinit var noteViewModel: NoteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(bottomAppBar)

        AppCenter.start(
            application, "dd844409-6204-48a0-8c0e-fb4e100c0bb7",
            Analytics::class.java, Crashes::class.java
        )

        recyclerView = findViewById(R.id.recyclerview)
        recyclerView.adapter = adapter
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)

        noteViewModel = ViewModelProvider(this).get(NoteViewModel::class.java)
        noteViewModel.allNotes.observe(this, Observer { notes ->
            notes?.let {
                adapter.setNotes(it)
            }
        })

        adapter.setOnClickListener(object : NoteListAdapter.OnItemClickListener {
            override fun onItemClick(note: Note) {
                val intent = Intent(this@MainActivity, NewNoteActivity::class.java)
                intent.putExtra(NewNoteActivity.Extra_ID, note.id)
                intent.putExtra(NewNoteActivity.Extra_Title, note.noteTitle)
                intent.putExtra(NewNoteActivity.Extra_Text, note.noteText)
                startActivityForResult(intent, EDIT_NOTE_REQUEST)
            }

        })

        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            val intent = Intent(this@MainActivity, NewNoteActivity::class.java)
            startActivityForResult(intent, ADD_NOTE_REQUEST)
        }

        val itemTouchCallback = object : ItemTouchHelper.SimpleCallback(
            0, ItemTouchHelper.LEFT.or(
                ItemTouchHelper.RIGHT
            )
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                noteViewModel.deleteNote(adapter.getNoteAt(viewHolder.adapterPosition))
                Toast.makeText(this@MainActivity, R.string.toast_note_deleted, Toast.LENGTH_SHORT).show()
            }

        }

        val itemTouchHelper = ItemTouchHelper(itemTouchCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter.filter(newText)
                return false
            }

        })

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ADD_NOTE_REQUEST && resultCode == Activity.RESULT_OK) {

            val note = Note(data?.getStringExtra(NewNoteActivity.Extra_Title)!!,
                data.getStringExtra(NewNoteActivity.Extra_Text)!!,
                Calendar.getInstance().time,
                Category(data.getStringExtra(NewNoteActivity.Extra_Category_Name),
                    data.getIntExtra(NewNoteActivity.Extra_Category_Color, 0))
            )

            noteViewModel.insertNote(note)
            Toast.makeText(this, R.string.toast_note_saved, Toast.LENGTH_SHORT).show()

        } else if (requestCode == EDIT_NOTE_REQUEST && resultCode == Activity.RESULT_OK) {
            val id = data?.getIntExtra(NewNoteActivity.Extra_ID, -1)
            if (id == -1) {
                Toast.makeText(this, R.string.toast_note_cant_update, Toast.LENGTH_SHORT).show()
                return
            }

            val note = Note(data?.getStringExtra(NewNoteActivity.Extra_Title)!!,
                data.getStringExtra(NewNoteActivity.Extra_Text)!!,
                Calendar.getInstance().time,
                Category(data.getStringExtra(NewNoteActivity.Extra_Category_Name),
                    data.getIntExtra(NewNoteActivity.Extra_Category_Color, 0))
            )

            note.id = id!!
            noteViewModel.updateNote(note)

            Toast.makeText(this, R.string.toast_note_updated, Toast.LENGTH_SHORT).show()
        } else {

            Toast.makeText(this, R.string.toast_note_isnt_saved, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.bottom_nav_drawer_menu, menu)

        var categoriesList : List<Category>
        val categoryViewModel: CategoryViewModel = ViewModelProvider(this).get(CategoryViewModel::class.java)
        categoryViewModel.allCategories.observe(this, Observer<List<Category>> { categories ->
            categoriesList = categories
            menu?.clear()
            menu?.add(Menu.NONE, 0, Menu.NONE, R.string.all_categories)
            for ((index, value) in categoriesList.withIndex()) {
                menu?.add(Menu.NONE, index + 1, Menu.NONE, value.nameCategory)
            }

        })
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == 0) {
            noteViewModel.allNotes.observe(this, Observer { notes ->
                notes?.let {
                    adapter.setNotes(it)
                }
            })
        } else {
            noteViewModel.allNotes.observe(this, Observer { notes ->
                notes?.let {
                    adapter.setNotesByCategory(it, item.title.toString())
                }
            })
        }

        return true
    }

}



