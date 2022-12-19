package com.example.imageviewer.view.utils

import android.content.res.Resources
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
) : PagingDataAdapter<CatImage, ImagePagerAdapter.ImageHolder>(ImageDiffItemCallback) {

    private var recycler: RecyclerView? = null

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

    inner class ImageHolder(private val binding: OpenedImageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private var buttonFaded = true
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

            binding.image.nextButton.setOnClickListener {
                if (absoluteAdapterPosition >= itemCount - 1) return@setOnClickListener
                onImageWatched?.invoke(image ?: return@setOnClickListener, absoluteAdapterPosition)
                recycler?.smoothScrollToPosition(absoluteAdapterPosition + 1)
            }
            binding.image.previousButton.setOnClickListener {
                if (absoluteAdapterPosition <= 0) return@setOnClickListener
                onImageWatched?.invoke(image ?: return@setOnClickListener, absoluteAdapterPosition)
                recycler?.smoothScrollToPosition(absoluteAdapterPosition - 1)
            }
            binding.image.fadeButton.setOnClickListener {
                onFadeButtonClick(500L)
            }
            binding.image.favoriteButton.setOnClickListener {
                if (image == null) return@setOnClickListener
                favoriteButtonListener?.invoke(image!!, absoluteAdapterPosition)
            }
            binding.image.likeButton.setOnClickListener {
                if (image == null) return@setOnClickListener
                likeButtonListener?.invoke(image!!, absoluteAdapterPosition)
            }
            binding.image.shareButton.setOnClickListener {
                ContextHelper.shareImage(binding.root.context, image)
            }
            binding.image.loadButton.setOnClickListener {
                ContextHelper.loadImage(binding.root.context, image)
            }
            binding.image.laterButton.setOnClickListener {
                ContextHelper.setAlarm(binding.root.context, image)
            }
        }

        fun onBind(catImage: CatImage?) {
            image = catImage
            if (!buttonFaded) {
                onFadeButtonClick(0)
            }
            if (catImage == null) return

            Glide.with(binding.root)
                .load(Uri.parse(catImage.url))
                .into(binding.image.image)



            binding.details.tags.chipGroup.forEachIndexed { index, view ->
                if (view is Chip) {
                    val name = catImage.categories.getOrNull(index)?.name
                    if (name.isNullOrEmpty()) view.visibility = View.GONE
                    else {
                        view.text = name
                        view.visibility = View.VISIBLE
                    }
                }
            }
            val formatter = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT)
            val liked = formatter.format(Date(catImage.liked))
            val watched = formatter.format(Date(catImage.watched))
            binding.details.liked.text = binding.root.resources.getString(R.string.liked, liked)
            binding.details.watched.text =
                binding.root.resources.getString(R.string.watched, watched)
            adapter.list = catImage.breeds
        }

        private fun onFadeButtonClick(animationsDuration: Long) {
            val direction = if (buttonFaded) -1 else 1
            binding.image.fadeButton.animate().translationYBy((direction * 272).px)
                .rotationBy(180.0f).setDuration(animationsDuration).start()
            binding.image.laterButton.animate().translationYBy((direction * 272).px)
                .alpha(if (buttonFaded) 1.0f else 0.5f).setDuration(animationsDuration).start()
            binding.image.loadButton.animate().translationYBy((direction * 204).px)
                .alpha(if (buttonFaded) 1.0f else 0.5f).setDuration(animationsDuration).start()
            binding.image.shareButton.animate().translationYBy((direction * 136).px)
                .alpha(if (buttonFaded) 1.0f else 0.5f).setDuration(animationsDuration).start()
            binding.image.favoriteButton.animate().translationYBy((direction * 68).px)
                .alpha(if (buttonFaded) 1.0f else 0.5f).setDuration(animationsDuration).start()
            buttonFaded = !buttonFaded
        }
    }
}

object ImageDiffItemCallback : DiffUtil.ItemCallback<CatImage>() {
    override fun areItemsTheSame(oldItem: CatImage, newItem: CatImage) = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: CatImage, newItem: CatImage) =
        oldItem.url == newItem.url && oldItem.liked == newItem.liked
                && oldItem.watched == newItem.watched && oldItem.favorite == newItem.favorite
}

val Int.px: Float
    get() = (this * Resources.getSystem().displayMetrics.density)

