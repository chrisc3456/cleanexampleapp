package com.example.clean

import android.view.View
import com.example.clean.viewobjects.Summary
import com.example.clean.databinding.ItemSummaryBinding
import com.example.clean.ui.fragments.summarylist.SummaryViewHolder
import com.nhaarman.mockitokotlin2.mock
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*

class SummaryViewHolderTest {

    @Mock private lateinit var view: View
    @Mock private lateinit var binding: ItemSummaryBinding
    @Mock private lateinit var onClickListener: (String) -> Unit
    private lateinit var holder: SummaryViewHolder

    @Before
    fun setup() {
        view = mock(View::class.java)
        onClickListener = mock()
        binding = mock(ItemSummaryBinding::class.java)
        `when`(binding.root).thenReturn(view)

        holder = SummaryViewHolder(binding, onClickListener)
    }

    @Test
    fun bindItem_withSummary_bindingPropertiesSet() {
        val summary = Summary("", "", "", "")
        holder.bindItem(summary)
        verify(binding).summary = summary
    }
}