package com.example.imageviewer.view.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagingData
import com.example.imageviewer.databinding.FragmentImagePagerBinding
import com.example.imageviewer.domain.CatImage
import com.example.imageviewer.view.utils.ImagePagerAdapter
import com.example.imageviewer.view.utils.ImagePagerLayoutManager
import com.example.imageviewer.viewModel.ImagePagerViewModel
import com.example.imageviewer.source.web.WebServiceImpl
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ImagePagerFragment(images: StateFlow<PagingData<CatImage>>? = null) : Fragment() {

    private val binding by lazy {
        FragmentImagePagerBinding.inflate(layoutInflater)
    }

    private val viewModel: ImagePagerViewModel = ImagePagerViewModel(WebServiceImpl(), images)

    private val recyclerAdapter: ImagePagerAdapter by lazy {
        ImagePagerAdapter(binding.recycler)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val layoutManager = ImagePagerLayoutManager(requireContext())

        binding.recycler.layoutManager = layoutManager
        binding.recycler.adapter = recyclerAdapter

        lifecycleScope.launch {
            viewModel.images.collect {
                recyclerAdapter.submitData(it)
            }
        }

        binding.progressBar.visibility =
            if (recyclerAdapter.itemCount == 0) View.VISIBLE else View.GONE

        recyclerAdapter.addOnPagesUpdatedListener {
            binding.progressBar.visibility =
                if (recyclerAdapter.itemCount == 0) View.VISIBLE else View.GONE
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }


}