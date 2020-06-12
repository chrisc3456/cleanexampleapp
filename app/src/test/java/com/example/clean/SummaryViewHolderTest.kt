package com.example.clean

import android.view.View
import com.example.clean.data.model.Summary
import com.example.clean.databinding.ItemSummaryBinding
import com.example.clean.ui.summarylist.SummaryViewHolder
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*

class SummaryViewHolderTest {

    @Mock private lateinit var view: View
    @Mock private lateinit var binding: ItemSummaryBinding
    private lateinit var holder: SummaryViewHolder

    @Before
    fun setup() {
        view = mock(View::class.java)
        binding = mock(ItemSummaryBinding::class.java)
        `when`(binding.root).thenReturn(view)

        holder = SummaryViewHolder(binding)
    }

    @Test
    fun bindItem_withSummary_bindingSummarySet() {
        val summary = Summary("", "", "", "")
        holder.bindItem(summary)
        verify(binding).summary = summary
    }
}