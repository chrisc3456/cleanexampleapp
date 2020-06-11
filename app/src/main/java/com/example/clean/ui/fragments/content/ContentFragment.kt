package com.example.clean.ui.fragments.content

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.clean.R
import com.example.clean.ViewModelFactory
import com.example.clean.databinding.FragmentContentBinding
import com.example.clean.ui.fragments.BaseFragment
import com.example.clean.viewmodels.ContentViewModel
import com.example.clean.viewobjects.Content
import kotlinx.android.synthetic.main.fragment_content.*
import javax.inject.Inject

class ContentFragment : BaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var contentViewModel: ContentViewModel
    private lateinit var contentBinding: FragmentContentBinding
    private var itemId: String = ""

    override fun onAttach(context: Context) {
        super.onAttach(context)
        app.cleanAppComponent.inject(this@ContentFragment)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        contentViewModel = ViewModelProvider(this, viewModelFactory).get(ContentViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_content, container, false)
        setupObservers()
        setupBindings(view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        itemId = ContentFragmentArgs.fromBundle(requireArguments()).id
        contentViewModel.getContent(itemId)
        setupToolbar()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> requireActivity().onBackPressedDispatcher.onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupObservers() {
        contentViewModel.content.observe(viewLifecycleOwner, Observer { content -> onContentUpdated(content) })
        contentViewModel.isDataLoading.observe(viewLifecycleOwner, Observer { isDataLoading -> onDataLoadingChange(isDataLoading) })
        contentViewModel.isLoadingError.observe(viewLifecycleOwner, Observer { isLoadingError -> onDataLoadingError(isLoadingError) })
    }

    private fun setupBindings(view: View) {
        contentBinding = FragmentContentBinding.bind(view)
        contentBinding.lifecycleOwner = viewLifecycleOwner
    }

    private fun setupToolbar() {
        setHasOptionsMenu(true)

        (requireActivity() as AppCompatActivity).apply {
            setSupportActionBar(toolbarContent)
            setupActionBarWithNavController(findNavController())

            supportActionBar?.apply {
                title = "Item $itemId"
                setDisplayShowTitleEnabled(true)
                setDisplayHomeAsUpEnabled(true)
                setHomeAsUpIndicator(R.drawable.ic_arrow_back)
            }
        }
    }

    private fun onContentUpdated(content: Content) {
        contentBinding.content = content
    }

    private fun onDataLoadingChange(isDataLoading: Boolean) {
        if (isDataLoading) {
            progressBarContent.visibility = View.VISIBLE
        } else {
            progressBarContent.visibility = View.GONE
        }
    }

    private fun onDataLoadingError(isLoadingError: Boolean) {
        if (isLoadingError) {
            val toast = Toast.makeText(requireContext(), resources.getString(R.string.errorNoData), Toast.LENGTH_LONG)
            toast.show()
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }
}