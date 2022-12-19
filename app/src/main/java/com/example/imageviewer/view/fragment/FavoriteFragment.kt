package com.example.imageviewer.view.fragment

import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.imageviewer.App
import com.example.imageviewer.databinding.FragmentFavoriteBinding
import com.example.imageviewer.domain.CatImage
import com.example.imageviewer.view.utils.*
import kotlinx.coroutines.launch

class FavoriteFragment : Fragment() {

    var bundleImage: CatImage? = null

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
        ImagePagerAdapter(upButtonListener = closeImage(),
            favoriteButtonListener = { image, _ ->
                viewModel.updateFavorite(image)
            },
            likeButtonListener = { image, _ ->
                viewModel.updateLiked(image)
            },
            onImageWatched = { image, _ ->
                viewModel.updateWatched(image)
            },
            setAlarm = { image ->
                ContextHelper.updateAlarm(requireContext(), image, viewModel)
            })
    }

    private val openedRecyclerLayoutManager by lazy {
        ImagePagerLayoutManager(requireContext())
    }

    private val itemDecorator by lazy {
        ImageGridDecorator(requireContext())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bundleImage = arguments?.getParcelable(ContextHelper.CAT_IMAGE_PARCEL)
        Log.i("VVV", bundleImage.toString())

        binding.gridRecycler.adapter = gridAdapter
        binding.gridRecycler.addItemDecoration(itemDecorator)

        binding.openedRecycler.adapter = openedRecyclerAdapter
        binding.openedRecycler.layoutManager = openedRecyclerLayoutManager
        binding.openedRecycler.visibility = View.GONE

        updateCheckState()

        binding.chipGroup.setOnCheckedStateChangeListener { _, _ ->
            updateCheckState()
        }

        gridAdapter.addOnPagesUpdatedListener {
            gridAdapter.notifyDataSetChanged()
        }

        lifecycleScope.launch {
            viewModel.images.collect {
                gridAdapter.submitData(it)
                if (bundleImage != null) {
                    binding.gridRecycler.scrollToPosition(0)
                }
            }
        }

        lifecycleScope.launch {
            viewModel.images.collect {
                openedRecyclerAdapter.submitData(it)
                if (bundleImage != null) {
                    openImage().invoke(0)
                    bundleImage = null
                }
                if (openedRecyclerAdapter.itemCount == 0) closeImage().invoke()
            }
        }

        if (bundleImage != null) binding.alarmedChip.isChecked = true
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

    private fun updateCheckState() {
        viewModel.needFavorite = binding.favoriteChip.isChecked
        viewModel.needLiked = binding.likedChip.isChecked
        viewModel.needWatched = binding.watchedChip.isChecked
        viewModel.needAlarmed = binding.alarmedChip.isChecked
    }
}