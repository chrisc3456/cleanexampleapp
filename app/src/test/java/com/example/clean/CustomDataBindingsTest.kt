package com.example.clean

import android.content.Context
import android.widget.TextView
import com.example.clean.ui.bindTextDate
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

class CustomDataBindingsTest {

    private lateinit var context: Context
    private lateinit var textView: TextView

    @Before
    fun setup() {
        context = mock(Context::class.java)
        textView = mock(TextView::class.java)
    }

    @Test
    fun bindTextDate_noDate_textViewNotPopulated() {
        bindTextDate(textView, null)
        assertNull(textView.text)
    }

    @Test
    fun bindTextDate_dateProvided_textViewHasFormattedDate() {
        bindTextDate(textView, "2020-06-12T12:34:56")
        verify(textView).text = "Fri 12 Jun 2020"
    }
}