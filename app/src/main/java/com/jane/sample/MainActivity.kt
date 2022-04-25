package com.jane.sample

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.jane.sample.dialog.DialogActivity

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // dialog
        findViewById<TextView>(R.id.textView_dialog).setOnClickListener {
            startActivity(Intent(this, DialogActivity::class.java))
        }
    }
}