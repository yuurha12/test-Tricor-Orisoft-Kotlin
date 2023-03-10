package com.example.mydiary.ui.diary

import android.app.AlertDialog
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.mydiary.R
import com.example.mydiary.data.model.DiaryResponse
import com.example.mydiary.data.remote.DiaryApiClient
import com.example.mydiary.databinding.ActivityDiaryDetailBinding


@Suppress("WHEN_ENUM_CAN_BE_NULL_IN_JAVA", "DEPRECATION")
class DiaryDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDiaryDetailBinding
    private lateinit var viewModel: DiaryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDiaryDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val diaryId = intent.getLongExtra(EXTRA_DIARY_ID, -1L)
        viewModel = ViewModelProvider(this, DiaryViewModelFactory(DiaryApiClient.api, application))[DiaryViewModel::class.java]

        viewModel.diaryResponse.observe(this) { diary ->
            if (diary != null) {
                bindDiary(diary)
            } else {
                showErrorDialog()
            }
        }

        viewModel.loadingStatus.observe(this) { loadingStatus ->
            when (loadingStatus) {
                DiaryViewModel.LoadingStatus.LOADING -> binding.progressBar.visibility =
                    View.VISIBLE
                DiaryViewModel.LoadingStatus.SUCCESS -> binding.progressBar.visibility = View.GONE
                DiaryViewModel.LoadingStatus.ERROR -> {
                    binding.progressBar.visibility = View.GONE
                    showErrorDialog()
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                if (onSupportNavigateUp()) {
                    true
                } else {
                    super.onOptionsItemSelected(item)
                }
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun bindDiary(diaryResponse: DiaryResponse) {
        binding.diaryTitle.text = diaryResponse.title
        binding.diaryContent.text = diaryResponse.content
    }

    private fun showErrorDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage(getString(R.string.error_message))
        builder.setPositiveButton(getString(R.string.ok)) { dialog, _ ->
            dialog.dismiss()
            finish()
        }
        builder.setOnCancelListener {
            finish()
        }
        builder.show()
    }

    companion object {
        const val EXTRA_DIARY_ID = "extra_diary_id"
    }
}
