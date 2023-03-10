package com.example.mydiary.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.mydiary.R
import com.example.mydiary.data.remote.DiaryApiClient
import com.example.mydiary.ui.diary.DiaryActivity
import com.example.mydiary.ui.diary.DiaryViewModel
import com.example.mydiary.ui.diary.DiaryViewModelFactory


class LoginActivity : AppCompatActivity() {

    private lateinit var viewModel: DiaryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val diaryApiClient = DiaryApiClient.api
        val viewModelFactory = DiaryViewModelFactory(diaryApiClient, application)
        viewModel = ViewModelProvider(this, viewModelFactory)[DiaryViewModel::class.java]

        val loginButton = findViewById<Button>(R.id.login_button)
        val emailEditText = findViewById<EditText>(R.id.email_edit_text)
        val passwordEditText = findViewById<EditText>(R.id.password_edit_text)
        val registerTextView = findViewById<TextView>(R.id.register_text_view)

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            viewModel.login(email, password)
        }

        registerTextView.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        viewModel.loginResponse.observe(this) { response ->
            if (response.token != null) {
                Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, DiaryActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, response.error, Toast.LENGTH_SHORT).show()
            }
        }
    }
}
