package com.example.mydiary

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diary)
        setContentView(R.layout.activity_diary_form)
        setContentView(R.layout.activity_diary_detail)
        setContentView(R.layout.activity_login)
        setContentView(R.layout.activity_register)
        setContentView(R.layout.item_diary)
        setContentView(R.layout.pagination_item)
    }
}

