package com.example.mydiary.ui.diary

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mydiary.R
import com.example.mydiary.data.model.Diary
import com.example.mydiary.databinding.ItemDiaryBinding

class DiaryAdapter(
    private val onItemClick: (Diary) -> Unit,
    private val onArchiveButtonClick: (Diary) -> Unit,
    private val onUnarchiveButtonClick: (Diary) -> Unit
) : ListAdapter<Diary, DiaryAdapter.DiaryViewHolder>(DiaryDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiaryViewHolder {
        val binding = ItemDiaryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DiaryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DiaryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class DiaryViewHolder(private val binding: ItemDiaryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClick(getItem(position))
                }
            }
            binding.archiveButton.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val diary = getItem(position)
                    if (diary.isArchived) {
                        onUnarchiveButtonClick(diary)
                    } else {
                        onArchiveButtonClick(diary)
                    }
                }
            }
        }

        fun bind(diary: Diary) {
            binding.diaryTitle.text = diary.title
            binding.diaryContent.text = diary.content
            if (diary.isArchived) {
                binding.archiveButton.text = binding.root.context.getString(R.string.unarchive)
            } else {
                binding.archiveButton.text = binding.root.context.getString(R.string.archive)
            }
        }
    }
}

private class DiaryDiffCallback : DiffUtil.ItemCallback<Diary>() {
    override fun areItemsTheSame(oldItem: Diary, newItem: Diary): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Diary, newItem: Diary): Boolean {
        return oldItem == newItem
    }
}
