package com.example.imageviewer.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.App
import com.example.imageviewer.databinding.FragmentSearchImageBinding
import com.example.imageviewer.view.utils.ImageGridAdapter
import com.example.imageviewer.view.utils.ImageGridDecorator
import com.example.imageviewer.view.utils.ImagePagerAdapter
import com.example.imageviewer.view.utils.ImagePagerLayoutManager
import kotlinx.coroutines.launch

class SearchFragment() : Fragment() {

    private val binding by lazy {
        FragmentSearchImageBinding.inflate(layoutInflater)
    }

    private val viewModel = App.appComponent.searchViewModel

    private val gridRecyclerAdapter by lazy {
        ImageGridAdapter(openImage())
    }

    private val openedRecyclerAdapter by lazy {
        ImagePagerAdapter(
            binding.openedRecycler,
            upButtonListener = closeImage(),
            favoriteButtonListener = viewModel.favoriteButtonListener,
            likeButtonListener = viewModel.likeButtonListener
        )
    }

    private val openedRecyclerLayoutManager by lazy {
        ImagePagerLayoutManager(requireContext())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.query = newText
                return true
            }
        })

        binding.gridRecycler.addItemDecoration(ImageGridDecorator(requireContext()))
        binding.gridRecycler.adapter = gridRecyclerAdapter
        binding.openedRecycler.adapter = openedRecyclerAdapter
        binding.openedRecycler.layoutManager = openedRecyclerLayoutManager

        lifecycleScope.launch {
            viewModel.images.collect {
                openedRecyclerAdapter.submitData(it)
            }
        }
        lifecycleScope.launch {
            viewModel.images.collect {
                gridRecyclerAdapter.submitData(it)
            }
        }

        binding.progressBar.visibility =
            if (gridRecyclerAdapter.itemCount == 0) View.VISIBLE else View.GONE

        gridRecyclerAdapter.addOnPagesUpdatedListener {
            binding.progressBar.visibility =
                if (gridRecyclerAdapter.itemCount == 0) View.VISIBLE else View.GONE
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    private fun openImage(): (position: Int) -> Unit = {
        binding.openedRecycler.scrollToPosition(it)
        binding.openedRecycler.visibility = View.VISIBLE
    }

    private fun closeImage(): () -> Unit = {
        binding.openedRecycler.visibility = View.GONE
    }
}