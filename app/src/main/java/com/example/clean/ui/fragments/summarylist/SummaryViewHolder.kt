package com.example.clean.ui.fragments.summarylist

import androidx.recyclerview.widget.RecyclerView
import com.example.clean.databinding.ItemSummaryBinding
import com.example.clean.viewobjects.Summary

class SummaryViewHolder(private val binding: ItemSummaryBinding, private val onClickSummaryListener: (String) -> Unit): RecyclerView.ViewHolder(binding.root) {
    fun bindItem(summary: Summary) {
        binding.summary = summary
        binding.root.setOnClickListener { onClickSummaryListener(summary.id) }
    }
}