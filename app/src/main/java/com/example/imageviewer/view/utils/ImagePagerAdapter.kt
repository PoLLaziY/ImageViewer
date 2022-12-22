package com.example.imageviewer.view.utils

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.forEachIndexed
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.imageviewer.R
import com.example.imageviewer.databinding.OpenedImageBinding
import com.example.imageviewer.domain.CatImage
import com.google.android.material.chip.Chip
import java.text.DateFormat
import java.util.*


class ImagePagerAdapter(
    private inline val upButtonListener: (() -> Unit)? = null,
    private inline val downButtonListener: (() -> Unit)? = null,
    private inline val favoriteButtonListener: ((image: CatImage, position: Int) -> Unit)? = null,
    private inline val likeButtonListener: ((image: CatImage, position: Int) -> Unit)? = null,
    private inline val onImageWatched: ((image: CatImage, position: Int) -> Unit)? = null,
    private inline val setAlarm: ((image: CatImage) -> Unit)? = null
) : PagingDataAdapter<CatImage, ImagePagerAdapter.ImageHolder>(ImageDiffItemCallback) {

    private var recycler: RecyclerView? = null
    private val formatter: DateFormat by lazy {
        DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        recycler = recyclerView
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        recycler = null
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = OpenedImageBinding.inflate(inflater, parent, false)
        return ImageHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageHolder, position: Int) {
        holder.onBind(getItem(position))
    }

    fun findPosition(image: CatImage?): Int {
        if (image == null) return 0
        for (i in 0 until itemCount) {
            if (getItem(i)?.id == image.id) return i
        }
        return 0
    }

    inner class ImageHolder(private val binding: OpenedImageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        var image: CatImage? = null
        private val adapter = BreedListAdapter()

        init {
            binding.details.recycler.adapter = adapter
            if (upButtonListener == null) binding.image.upButton.visibility = View.GONE
            else binding.image.upButton.setOnClickListener {
                upButtonListener.invoke()
                onImageWatched?.invoke(image ?: return@setOnClickListener, absoluteAdapterPosition)
            }

            if (downButtonListener == null) binding.image.downButton.visibility = View.GONE
            else binding.image.downButton.setOnClickListener {
                downButtonListener.invoke()
            }

            binding.controlPanel.nextButton.setOnClickListener {
                if (absoluteAdapterPosition >= itemCount - 1) return@setOnClickListener
                onImageWatched?.invoke(image ?: return@setOnClickListener, absoluteAdapterPosition)
                recycler?.smoothScrollToPosition(absoluteAdapterPosition + 1)
            }
            binding.controlPanel.previousButton.setOnClickListener {
                if (absoluteAdapterPosition <= 0) return@setOnClickListener
                onImageWatched?.invoke(image ?: return@setOnClickListener, absoluteAdapterPosition)
                recycler?.smoothScrollToPosition(absoluteAdapterPosition - 1)
            }
            binding.controlPanel.favoriteButton.setOnClickListener {
                if (image != null) favoriteButtonListener?.invoke(image!!, absoluteAdapterPosition)
            }
            binding.controlPanel.likeButton.setOnClickListener {
                if (image != null) likeButtonListener?.invoke(image!!, absoluteAdapterPosition)
            }
            binding.controlPanel.shareButton.setOnClickListener {
                ContextHelper.shareImage(binding.root.context, image)
            }
            binding.controlPanel.loadButton.setOnClickListener {
                ContextHelper.loadImage(binding.root.context, image)
            }
            binding.controlPanel.laterButton.setOnClickListener {
                if (image != null) setAlarm?.invoke(image!!)
            }
        }

        fun onBind(catImage: CatImage?) {
            image = catImage
            if (catImage == null) return

            Glide.with(binding.root)
                .load(Uri.parse(catImage.url))
                .into(binding.image.image)

            binding.details.tags.chipGroup.forEachIndexed { index, view ->
                if (view is Chip) {
                    val name = catImage.categories?.getOrNull(index)?.name
                    if (name.isNullOrEmpty()) view.visibility = View.GONE
                    else {
                        view.text = name
                        view.visibility = View.VISIBLE
                    }
                }
            }
            val liked = if (catImage.liked == 0L) '-' else formatter.format(Date(catImage.liked))
            val watched =
                if (catImage.watched == 0L) "today" else formatter.format(Date(catImage.watched))
            val alarmed =
                if (catImage.alarmTime == 0L) '-' else formatter.format(Date(catImage.alarmTime))
            binding.details.liked.text = binding.root.resources.getString(R.string.liked, liked)
            binding.details.watched.text =
                binding.root.resources.getString(R.string.watched, watched)
            binding.details.alarmed.text =
                binding.root.resources.getString(R.string.alarmed, alarmed)
            adapter.list = catImage.breeds
        }
    }
}

object ImageDiffItemCallback : DiffUtil.ItemCallback<CatImage>() {
    override fun areItemsTheSame(oldItem: CatImage, newItem: CatImage) = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: CatImage, newItem: CatImage) =
        oldItem.url == newItem.url && oldItem.liked == newItem.liked
                && oldItem.watched == newItem.watched && oldItem.favorite == newItem.favorite
}
