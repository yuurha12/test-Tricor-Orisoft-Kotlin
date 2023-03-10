package com.example.mydiary.ui.diary

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.mydiary.R
import com.example.mydiary.data.model.Diary
import com.example.mydiary.data.remote.DiaryApiClient
import com.example.mydiary.databinding.ActivityDiaryFormBinding

class DiaryFormActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_DIARY_ID = "com.example.mydiary.data.EXTRA_DIARY_ID"
    }

    private lateinit var binding: ActivityDiaryFormBinding

    private val viewModel: DiaryViewModel by viewModels {
        DiaryViewModelFactory(DiaryApiClient.api, application)
    }

    private var diaryId: Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_diary_form)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        diaryId = intent.getLongExtra(EXTRA_DIARY_ID, 0L)

        if (diaryId != null && diaryId != 0L) {
            viewModel.getDiaryById(diaryId!!)
            supportActionBar?.title = getString(R.string.title_edit_diary)
        } else {
            supportActionBar?.title = getString(R.string.title_add_diary)
        }

        viewModel.diaryResponse.observe(this) { diary ->
            if (diary != null) {
                binding.diaryTitle.setText(diary.title)
                binding.diaryContent.setText(diary.content)
            }
        }

        binding.saveButton.setOnClickListener {
            saveDiary()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_diary_form, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
            R.integer.delete_diary -> {
                showDeleteConfirmationDialog()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun saveDiary() {
        val title = binding.diaryTitle.text.toString().trim()
        val content = binding.diaryContent.text.toString().trim()

        if (title.isEmpty() && content.isEmpty()) {
            Toast.makeText(this, getString(R.string.toast_empty_fields), Toast.LENGTH_SHORT).show()
            return
        }

        val diary = Diary(
            title = title,
            content = content,
            id = diaryId ?: 0L,
            isArchived = false
        )

        if (diaryId == null) {
            viewModel.createDiary(diary)
        } else {
            viewModel.updateDiary(diaryId!!, diary)
        }

        Toast.makeText(this, getString(R.string.toast_saved), Toast.LENGTH_SHORT).show()
        finish()
    }


    private fun showDeleteConfirmationDialog() {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.dialog_title_delete_diary))
            .setMessage(getString(R.string.dialog_message_delete_diary))
            .setPositiveButton(getString(R.string.dialog_button_delete)) { _, _ ->
                viewModel.deleteDiary(diaryId!!)
                Toast.makeText(this, getString(R.string.toast_deleted), Toast.LENGTH_SHORT).show()
                finish()
            }
            .setNegativeButton(getString(R.string.dialog_button_cancel)) { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }
}
