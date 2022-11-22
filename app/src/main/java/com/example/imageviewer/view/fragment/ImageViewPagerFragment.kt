package com.example.imageviewer.view.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.imageviewer.databinding.FragmentImageViewPagerBinding
import com.example.imageviewer.view.ImagePagerAdapter
import com.example.imageviewer.view.ImagePagerLayoutManager
import com.example.imageviewer.web.WebServiceImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ImageViewPagerFragment : Fragment() {

    private val binding by lazy {
        FragmentImageViewPagerBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val service = WebServiceImpl()

        val layoutManager = ImagePagerLayoutManager(requireContext())

        val adapter = ImagePagerAdapter(binding.recycler)
        binding.recycler.layoutManager = layoutManager
        binding.recycler.adapter = adapter

        CoroutineScope(Dispatchers.IO).launch {
            val list = service.getNewPublicImages(0, 10)
            Log.i("Web", "$list")
            CoroutineScope(Dispatchers.Main).launch {
                adapter.list = list
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