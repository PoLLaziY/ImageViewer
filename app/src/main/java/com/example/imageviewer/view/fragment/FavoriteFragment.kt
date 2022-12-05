package com.example.imageviewer.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.App
import com.example.imageviewer.databinding.FragmentFavoriteBinding
import com.example.imageviewer.view.utils.ImageGridAdapter
import com.example.imageviewer.view.utils.ImageGridDecorator
import com.example.imageviewer.view.utils.ImagePagerAdapter
import com.example.imageviewer.view.utils.ImagePagerLayoutManager
import kotlinx.coroutines.launch

class FavoriteFragment : Fragment() {

    private val binding by lazy {
        FragmentFavoriteBinding.inflate(layoutInflater)
    }

    private val viewModel by lazy {
        App.appComponent.favoriteViewModel
    }

    private val gridAdapter by lazy {
        ImageGridAdapter(openImage = openImage())
    }

    private val openedRecyclerAdapter by lazy {
        ImagePagerAdapter(binding.openedRecycler,
            upButtonListener = closeImage())
    }

    private val openedRecyclerLayoutManager by lazy {
        ImagePagerLayoutManager(requireContext())
    }

    private val itemDecorator by lazy {
        ImageGridDecorator(requireContext())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.gridRecycler.adapter = gridAdapter
        binding.gridRecycler.addItemDecoration(itemDecorator)

        binding.openedRecycler.adapter = openedRecyclerAdapter
        binding.openedRecycler.layoutManager = openedRecyclerLayoutManager
        binding.openedRecycler.visibility = View.GONE

        lifecycleScope.launch {
            viewModel.images.collect {
                gridAdapter.submitData(it)
            }
        }

        lifecycleScope.launch {
            viewModel.images.collect {
                openedRecyclerAdapter.submitData(it)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    private fun openImage(): (Int) -> Unit = {
        binding.openedRecycler.scrollToPosition(it)
        binding.openedRecycler.visibility = View.VISIBLE
    }

    private fun closeImage(): () -> Unit = {
        binding.openedRecycler.visibility = View.GONE
    }
}