package com.example.clean.ui.fragments.summarylist

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.clean.R
import com.example.clean.ViewModelFactory
import com.example.clean.ui.VerticalListSpacer
import com.example.clean.ui.fragments.BaseFragment
import com.example.clean.viewmodels.SummaryListViewModel
import com.example.clean.viewobjects.Summary
import kotlinx.android.synthetic.main.fragment_summary_list.*
import javax.inject.Inject

class SummaryListFragment : BaseFragment() {

    @Inject lateinit var viewModelFactory: ViewModelFactory
    private lateinit var summaryViewModel: SummaryListViewModel
    private val summaryAdapter = SummaryListAdapter { id -> onSummaryClick(id) }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        app.cleanAppComponent.inject(this@SummaryListFragment)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        summaryViewModel = ViewModelProvider(this, viewModelFactory).get(SummaryListViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_summary_list, container, false)
        setupObservers()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecycler()
        setupSwipeToRefresh()
    }

    private fun setupRecycler() {
        recyclerSummaryItems.apply {
            adapter = summaryAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            addItemDecoration(VerticalListSpacer(requireContext(), R.drawable.list_spacer_vertical, R.drawable.list_spacer_horizontal, true))
        }
    }

    private fun setupSwipeToRefresh() {
        swipeSummaryList.setOnRefreshListener {
            summaryViewModel.loadItems(true)
        }
    }

    private fun setupObservers() {
        summaryViewModel.items.observe(viewLifecycleOwner, Observer { items -> onSummaryListUpdated(items) })
        summaryViewModel.isDataLoading.observe(viewLifecycleOwner, Observer { isDataLoading -> onDataLoadingChange(isDataLoading) })
        summaryViewModel.isLoadingError.observe(viewLifecycleOwner, Observer { isLoadingError -> onDataLoadingError(isLoadingError) })
    }

    private fun onSummaryListUpdated(summaries: List<Summary>) {
        summaryAdapter.setSummaries(summaries)
    }

    private fun onDataLoadingChange(isDataLoading: Boolean) {
        swipeSummaryList.isRefreshing = isDataLoading
    }

    private fun onDataLoadingError(isLoadingError: Boolean) {
        if (isLoadingError) {
            val toast = Toast.makeText(requireContext(), resources.getString(R.string.errorNoData), Toast.LENGTH_LONG)
            toast.show()
        }
    }

    private fun onSummaryClick(id: String) {
        val action = SummaryListFragmentDirections.actionSummaryListFragmentToFullContentFragment(id)
        findNavController().navigate(action)
    }
}