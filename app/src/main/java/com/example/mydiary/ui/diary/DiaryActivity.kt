package com.example.mydiary.ui.diary

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mydiary.R
import com.example.mydiary.data.model.Diary
import com.example.mydiary.data.remote.DiaryApiClient
import com.example.mydiary.databinding.ActivityDiaryBinding

class DiaryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDiaryBinding
    private lateinit var viewModel: DiaryViewModel
    private lateinit var adapter: DiaryAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDiaryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel =
            ViewModelProvider(this, DiaryViewModelFactory(DiaryApiClient.api, application))[DiaryViewModel::class.java]


        adapter = DiaryAdapter(
            this::onDiaryItemClick,
            this::onArchiveButtonClick,
            this::onUnarchiveButtonClick
        )
        binding.diaryList.adapter = adapter
        binding.diaryList.layoutManager = LinearLayoutManager(this)


        viewModel.diaryList.observe(this) {
            adapter.submitList(it)
        }


        viewModel.loadingStatus.observe(this) { loadingStatus ->
            when (loadingStatus) {
                DiaryViewModel.LoadingStatus.LOADING -> binding.progressBar.visibility = View.VISIBLE
                DiaryViewModel.LoadingStatus.SUCCESS -> binding.progressBar.visibility = View.GONE
                DiaryViewModel.LoadingStatus.ERROR -> {
                    binding.progressBar.visibility = View.GONE
                    showErrorDialog()
                }
                else -> {}
            }
        }


        viewModel.searchDiaries.observe(this) { query ->
            viewModel.searchDiaries(query)
        }



        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    viewModel.searchQuery(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                viewModel.searchQuery(newText)
                return false
            }
        })


        binding.fab.setOnClickListener {
            startActivity(Intent(this, DiaryFormActivity::class.java))
        }
    }

    private fun onDiaryItemClick(diary: Diary) {
        val intent = Intent(this, DiaryDetailActivity::class.java)
        intent.putExtra(DiaryDetailActivity.EXTRA_DIARY_ID, diary.id)
        startActivity(intent)
    }

    private fun onArchiveButtonClick(diary: Diary) {
        viewModel.archiveDiary(diary.id)
    }

    private fun onUnarchiveButtonClick(diary: Diary) {
        viewModel.unarchiveDiary(diary.id)
    }

    private fun showErrorDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage(getString(R.string.error_message))
        builder.setPositiveButton(getString(R.string.ok)) { dialog, _ ->
            dialog.dismiss()
        }
        builder.show()
    }
}