package com.example.clean.ui

import android.widget.TextView
import androidx.databinding.BindingAdapter
import java.text.SimpleDateFormat
import java.util.*

@BindingAdapter("bindTextDate")
fun bindTextDate(textView: TextView, dateValue: String?) {
    val formatter = SimpleDateFormat("EEE, d MMM yyyy", Locale.getDefault())
    val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())

    dateValue?.let {
        val dateTime = parser.parse(it)
        dateTime?.let {
            textView.text = formatter.format(dateTime)
        }
    }
}