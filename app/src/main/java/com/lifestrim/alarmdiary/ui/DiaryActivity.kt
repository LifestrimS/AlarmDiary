package com.lifestrim.alarmdiary.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.lifestrim.alarmdiary.R
import com.lifestrim.alarmdiary.db.AppDatabase
import com.lifestrim.alarmdiary.db.dao.NoteDAO
import com.lifestrim.alarmdiary.db.entity.Note
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_diary.*


class DiaryActivity : AppCompatActivity() {

    private var db: AppDatabase? = null
    private var noteDao: NoteDAO? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diary)

        db = AppDatabase.getAppDatabase(context = this)

        Observable.fromCallable {
            db = AppDatabase.getAppDatabase(context = this)
            noteDao = db?.noteDao()

            val note1 = Note(0, "title0")
            val note2 = Note(1, "title1")

            with(noteDao) {
                this?.insertNote(note1)
                this?.insertNote(note2)
            }
            db?.noteDao()?.getAllNote()
        }.doOnNext { list ->
            var finalString = ""
            list?.map { finalString += it.noteTitle + " - " }
            tv_message.text = finalString

        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
    }
}