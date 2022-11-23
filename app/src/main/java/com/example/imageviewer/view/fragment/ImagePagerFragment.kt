package com.example.imageviewer.view.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.imageviewer.databinding.FragmentImagePagerBinding
import com.example.imageviewer.view.ImagePagerAdapter
import com.example.imageviewer.view.ImagePagerLayoutManager
import com.example.imageviewer.viewModel.ImagePagerViewModel
import com.example.imageviewer.web.WebServiceImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ImagePagerFragment : Fragment() {

    private val binding by lazy {
        FragmentImagePagerBinding.inflate(layoutInflater)
    }

    private val viewModel: ImagePagerViewModel by lazy {
        ImagePagerViewModel(WebServiceImpl())
    }

    private val recyclerAdapter: ImagePagerAdapter by lazy {
        ImagePagerAdapter(binding.recycler)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val layoutManager = ImagePagerLayoutManager(requireContext())

        binding.recycler.layoutManager = layoutManager
        binding.recycler.adapter = recyclerAdapter

        lifecycleScope.launch {
            viewModel.images.collectLatest {
                recyclerAdapter.submitData(it)
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