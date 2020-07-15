package com.lifestrim.alarmdiary.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
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
import com.lifestrim.alarmdiary.db.entity.Note
import com.lifestrim.alarmdiary.viewmodel.NoteViewModel
import java.util.*


class DiaryActivity : AppCompatActivity() {
    companion object {
        val ADD_NOTE_REQUEST = 1
        val EDIT_NOTE_REQUEST = 2
    }

    private lateinit var noteViewModel: NoteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diary)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        val adapter = NoteListAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        noteViewModel = ViewModelProvider(this).get(NoteViewModel::class.java)
        noteViewModel.allWords.observe(this, Observer { words ->
            // Update the cached copy of the words in the adapter.
            words?.let { adapter.setNotes(it) }
        })

        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            val intent = Intent(this@DiaryActivity, NewNoteActivity::class.java)
            startActivityForResult(intent, ADD_NOTE_REQUEST)
        }

        val itemTouchCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT.or(
            ItemTouchHelper.RIGHT)) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                noteViewModel.delete(adapter.getNoteAt(viewHolder.adapterPosition))
                Toast.makeText(this@DiaryActivity, "Note Deleted", Toast.LENGTH_SHORT).show()
            }

        }

        adapter.setOnClickListener(object : NoteListAdapter.OnItemClickListener {
            override fun onItemClick(note: Note) {
                val intent = Intent(this@DiaryActivity, NewNoteActivity::class.java)
                intent.putExtra(NewNoteActivity.Extra_ID, note.id)
                intent.putExtra(NewNoteActivity.Extra_Title, note.noteTitle)
                intent.putExtra(NewNoteActivity.Extra_Text, note.noteText)
                startActivityForResult(intent, EDIT_NOTE_REQUEST)
            }

        })


        val itemTouchHelper = ItemTouchHelper(itemTouchCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ADD_NOTE_REQUEST && resultCode == Activity.RESULT_OK) {
            val title = data?.getStringExtra(NewNoteActivity.Extra_Title)
            val description = data?.getStringExtra(NewNoteActivity.Extra_Text)
            val note = Note(title!!, description!!, Calendar.getInstance().time)
            noteViewModel.insert(note)
            Toast.makeText(this, "Note Saved", Toast.LENGTH_SHORT).show()
        } else if (requestCode == EDIT_NOTE_REQUEST && resultCode == Activity.RESULT_OK) {
            val id = data?.getIntExtra(NewNoteActivity.Extra_ID, -1)
            if (id == -1) {
                Toast.makeText(this, "Note Can't be updated", Toast.LENGTH_SHORT).show()
                return
            }
            val title = data?.getStringExtra(NewNoteActivity.Extra_Title)
            val description = data?.getStringExtra(NewNoteActivity.Extra_Text)
            val note = Note(title!!, description!!, Calendar.getInstance().time)
            note.id = id!!
            noteViewModel.update(note)
            Toast.makeText(this, "Note Updated", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "No Note Saved", Toast.LENGTH_SHORT).show()
        }
    }
}