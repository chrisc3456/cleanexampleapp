package com.example.clean.ui.fragments

import android.content.Context
import androidx.fragment.app.Fragment
import com.example.clean.CleanApp

abstract class BaseFragment: Fragment() {

    lateinit var app: CleanApp

    override fun onAttach(context: Context) {
        super.onAttach(context)
        app = requireActivity().application as CleanApp
    }
}