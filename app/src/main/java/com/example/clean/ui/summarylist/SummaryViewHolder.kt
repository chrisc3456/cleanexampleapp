package com.example.clean.ui.summarylist

import androidx.recyclerview.widget.RecyclerView
import com.example.clean.data.model.Summary
import com.example.clean.databinding.ItemSummaryBinding

class SummaryViewHolder(val binding: ItemSummaryBinding): RecyclerView.ViewHolder(binding.root) {
    fun bindItem(summary: Summary) {
        binding.summary = summary
    }
}