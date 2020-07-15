package com.lifestrim.alarmdiary.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.lifestrim.alarmdiary.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun testDatabase(view: View) {
        val intent = Intent(this, DiaryActivity::class.java)
        startActivity(intent)
    }


}