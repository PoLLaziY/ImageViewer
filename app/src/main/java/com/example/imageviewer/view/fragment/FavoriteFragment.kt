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
import kotlinx.coroutines.launch

class FavoriteFragment : Fragment() {

    private val binding by lazy {
        FragmentFavoriteBinding.inflate(layoutInflater)
    }

    private val viewModel by lazy {
        App.appComponent.favoriteViewModel
    }

    private val gridAdapter by lazy {
        ImageGridAdapter()
    }

    private val itemDecorator by lazy {
        ImageGridDecorator(requireContext())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.recycler.adapter = gridAdapter
        binding.recycler.addItemDecoration(itemDecorator)

        lifecycleScope.launch {
            viewModel.images.collect {
                gridAdapter.submitData(it)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }
}