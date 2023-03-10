package com.example.mydiary.ui.auth

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.mydiary.R
import com.example.mydiary.data.remote.DiaryApiClient
import com.example.mydiary.databinding.ActivityRegisterBinding
import com.example.mydiary.ui.diary.DiaryViewModel
import com.example.mydiary.ui.diary.DiaryViewModelFactory

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var viewModel: DiaryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(
            this,
            DiaryViewModelFactory(DiaryApiClient.api, application)
        )[DiaryViewModel::class.java]

        binding.btnRegister.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val username = binding.etUsername.text.toString()
            val password = binding.etPassword.text.toString()
            val passwordConfirmation = binding.etPasswordConfirmation.text.toString()

            if (email.isEmpty() || username.isEmpty() || password.isEmpty() || passwordConfirmation.isEmpty()) {
                Toast.makeText(this, R.string.empty_fields_error, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password != passwordConfirmation) {
                Toast.makeText(this, R.string.password_mismatch_error, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.register(username, email, password, passwordConfirmation)
            Toast.makeText(this, R.string.registration_success_message, Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}
