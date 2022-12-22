package com.example.imageviewer.view.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.imageviewer.App
import com.example.imageviewer.databinding.FragmentFavoriteBinding
import com.example.imageviewer.domain.CatImage
import com.example.imageviewer.domain.CatImageSnapshot
import com.example.imageviewer.view.utils.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class FavoriteFragment : Fragment() {

    private val jobs: MutableList<Job> = mutableListOf()

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

        val bundleImage: CatImage? =
            (arguments?.getParcelable(ContextHelper.CAT_IMAGE_PARCEL) as CatImageSnapshot?)?.current
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

        if (bundleImage != null) openBundleImage(bundleImage)
        else openCashedImages()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    private fun openCashedImages() {

        gridAdapter.addOnPagesUpdatedListener {
            gridAdapter.notifyDataSetChanged()
        }

        lifecycleScope.launch {
            viewModel.images.collect {
                gridAdapter.submitData(it)
            }
        }.save(jobs)

        lifecycleScope.launch {
            viewModel.images.collect {
                openedRecyclerAdapter.submitData(it)
            }
        }.save(jobs)
    }

    private fun openBundleImage(image: CatImage) {
        lifecycleScope.launch {
            binding.alarmedChip.isChecked = true
            viewModel.openedImageSource(image).collect {
                openedRecyclerAdapter.submitData(it)
                openImage().invoke(0)
            }
        }.save(jobs)
    }

    private fun openImage(): (Int) -> Unit = {
        binding.openedRecycler.scrollToPosition(it)
        binding.openedRecycler.visibility = View.VISIBLE
    }

    private fun closeImage(): () -> Unit = {
        binding.openedRecycler.visibility = View.GONE
        if (jobs.size == 1) {
            jobs.forEach { it.cancel() }
            openCashedImages()
        }
    }

    private fun updateCheckState() {
        viewModel.needFavorite = binding.favoriteChip.isChecked
        viewModel.needLiked = binding.likedChip.isChecked
        viewModel.needWatched = binding.watchedChip.isChecked
        viewModel.needAlarmed = binding.alarmedChip.isChecked
    }
}

fun Job.save(list: MutableList<Job>) {
    list.add(this)
}