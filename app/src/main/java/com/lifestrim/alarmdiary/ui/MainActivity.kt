package com.lifestrim.alarmdiary.ui

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

    fun toAlarm (view: View) {
        val intent = Intent(this, AlarmActivity::class.java)
        startActivity(intent)
    }
}