package com.example.clean.ui.summarylist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.clean.R
import com.example.clean.data.model.Summary

class SummaryListAdapter: RecyclerView.Adapter<SummaryViewHolder>() {

    private var summaries: List<Summary> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SummaryViewHolder {
        return SummaryViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_summary,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return summaries.size
    }

    override fun onBindViewHolder(holder: SummaryViewHolder, position: Int) {
        holder.bindItem(summaries[position])
    }

    fun setSummaries(summaries: List<Summary>) {
        this.summaries = summaries
        notifyDataSetChanged()
    }
}