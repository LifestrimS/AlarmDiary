package com.lifestrim.alarmdiary.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.lifestrim.alarmdiary.R
import com.lifestrim.alarmdiary.adapter.NoteListAdapter
import com.lifestrim.alarmdiary.db.entity.Category
import com.lifestrim.alarmdiary.db.entity.Note
import com.lifestrim.alarmdiary.fragment.BottomNavigationDrawerFragment
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

    private lateinit var noteViewModel: NoteViewModel

    private var currentFabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_CENTER

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(bottomAppBar)

        AppCenter.start(
            application, "dd844409-6204-48a0-8c0e-fb4e100c0bb7",
            Analytics::class.java, Crashes::class.java
        )

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        val adapter = NoteListAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        noteViewModel = ViewModelProvider(this).get(NoteViewModel::class.java)
        noteViewModel.allNotes.observe(this, Observer { notes ->
            // Update the cached copy of the notes in the adapter.
            notes?.let { adapter.setNotes(it) }
        })

        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            val intent = Intent(this@MainActivity, NewNoteActivity::class.java)
            startActivityForResult(intent, MainActivity.ADD_NOTE_REQUEST)
        }

        val itemTouchCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT.or(
            ItemTouchHelper.RIGHT)) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                noteViewModel.deleteNote(adapter.getNoteAt(viewHolder.adapterPosition))
                Toast.makeText(this@MainActivity, "Note Deleted", Toast.LENGTH_SHORT).show()
            }

        }

        adapter.setOnClickListener(object : NoteListAdapter.OnItemClickListener {
            override fun onItemClick(note: Note) {
                val intent = Intent(this@MainActivity, NewNoteActivity::class.java)
                intent.putExtra(NewNoteActivity.Extra_ID, note.id)
                intent.putExtra(NewNoteActivity.Extra_Title, note.noteTitle)
                intent.putExtra(NewNoteActivity.Extra_Text, note.noteText)
                startActivityForResult(intent, MainActivity.EDIT_NOTE_REQUEST)
            }

        })

        val itemTouchHelper = ItemTouchHelper(itemTouchCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)

    }



    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            android.R.id.home -> {
                val bottomNavDrawerFragment = BottomNavigationDrawerFragment()
                bottomNavDrawerFragment.show(supportFragmentManager, bottomNavDrawerFragment.tag)
            }
        }

        return true
    }

    private fun BottomAppBar.toggleFabAlignment() {
        currentFabAlignmentMode = fabAlignmentMode
        fabAlignmentMode = currentFabAlignmentMode.xor(1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == MainActivity.ADD_NOTE_REQUEST && resultCode == Activity.RESULT_OK) {
            val title = data?.getStringExtra(NewNoteActivity.Extra_Title)
            val description = data?.getStringExtra(NewNoteActivity.Extra_Text)

            val categoryName = data?.getStringExtra(NewNoteActivity.Extra_Category_Name)
            val categoryColor = data?.getIntExtra(NewNoteActivity.Extra_Category_Color, 0)
            val noteCategory = Category(categoryName, categoryColor)

            Log.d("TAG", "color in MainActivity: $categoryColor")

            val note = Note(title!!, description!!, Calendar.getInstance().time, noteCategory )

            noteViewModel.insertNote(note)
            Toast.makeText(this, "Note Saved", Toast.LENGTH_SHORT).show()
        } else if (requestCode == MainActivity.EDIT_NOTE_REQUEST && resultCode == Activity.RESULT_OK) {
            val id = data?.getIntExtra(NewNoteActivity.Extra_ID, -1)
            if (id == -1) {
                Toast.makeText(this, "Note can't be updated", Toast.LENGTH_SHORT).show()
                return
            }
            val title = data?.getStringExtra(NewNoteActivity.Extra_Title)
            val description = data?.getStringExtra(NewNoteActivity.Extra_Text)

            val categoryName = data?.getStringExtra(NewNoteActivity.Extra_Category_Name)
            val categoryColor = data?.getIntExtra(NewNoteActivity.Extra_Category_Color, 0)
            val noteCategory = Category(categoryName, categoryColor)

            val note = Note(title!!, description!!, Calendar.getInstance().time, noteCategory)
            note.id = id!!
            noteViewModel.updateNote(note)
            Toast.makeText(this, "Note Updated", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "No Note Saved", Toast.LENGTH_SHORT).show()
        }
    }


}



