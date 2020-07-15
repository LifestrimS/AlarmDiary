package com.lifestrim.alarmdiary.activity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.lifestrim.alarmdiary.R
import com.lifestrim.alarmdiary.util.logd
import com.lifestrim.alarmdiary.viewmodel.NoteViewModel
import kotlinx.android.synthetic.main.activity_new_note.*

class NewNoteActivity : AppCompatActivity() {

    companion object {
        val Extra_ID = "ExtraID"
        val Extra_Title = "ExtraTitle"
        val Extra_Text = "ExtraText"
    }

    private lateinit var noteViewModel: NoteViewModel

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_note)

        if (intent.hasExtra(Extra_ID)) {
            title = "Edit Note"
            edit_title.setText(intent.getStringExtra(Extra_Title))
            edit_text.setText(intent.getStringExtra(Extra_Text))
        } else {
            title = "Add Note"
        }

    }

    fun saveNote(view: View) {
        val title = edit_title.text.toString()
        val text = edit_text.text.toString()

        if (title.trim().isEmpty() || text.trim().isEmpty()) {
            Toast.makeText(this, "Please Enter a Title and Text", Toast.LENGTH_SHORT).show()
            return
        }
        val newIntent=Intent()
        newIntent.putExtra(Extra_Title, title)
        newIntent.putExtra(Extra_Text, text)
        val id=intent.getIntExtra(Extra_ID,-1)
        if (id!=-1){
            logd(id.toString())
            newIntent.putExtra(Extra_ID,id)
        }
        setResult(Activity.RESULT_OK, newIntent)
//        this checks if all the above things are done and sends result ok with the intent
        finish()
    }


}