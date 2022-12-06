package com.example.imageviewer.view.utils

import android.content.res.Resources
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.imageviewer.databinding.OpenedImageBinding
import com.example.imageviewer.domain.CatImage

class ImagePagerAdapter(
    private inline val upButtonListener: (() -> Unit)? = null,
    private inline val downButtonListener: (() -> Unit)? = null,
    private inline val favoriteButtonListener: ((image: CatImage) -> Unit)? = null,
    private inline val likeButtonListener: ((image: CatImage) -> Unit)? = null,
    private inline val onImageWatched: ((image: CatImage?) -> Unit)? = null,
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

        init {

            if (upButtonListener == null) binding.upButton.visibility = View.GONE
            else binding.upButton.setOnClickListener {
                upButtonListener.invoke()
                onImageWatched?.invoke(image)
            }

            if (downButtonListener == null) binding.downButton.visibility = View.GONE
            else binding.downButton.setOnClickListener {
                downButtonListener.invoke()
            }

            binding.nextButton.setOnClickListener {
                if (absoluteAdapterPosition >= itemCount - 1) return@setOnClickListener
                onImageWatched?.invoke(image)
                recycler?.smoothScrollToPosition(absoluteAdapterPosition + 1)
            }
            binding.previousButton.setOnClickListener {
                if (absoluteAdapterPosition <= 0) return@setOnClickListener
                onImageWatched?.invoke(image)
                recycler?.smoothScrollToPosition(absoluteAdapterPosition - 1)
            }
            binding.fadeButton.setOnClickListener {
                onFadeButtonClick(500L)
            }
            binding.favoriteButton.setOnClickListener {
                if (image == null) return@setOnClickListener
                favoriteButtonListener?.invoke(image!!)
            }
            binding.likeButton.setOnClickListener {
                if (image == null) return@setOnClickListener
                likeButtonListener?.invoke(image!!)
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
                .into(binding.image)
        }

        private fun onFadeButtonClick(animationsDuration: Long) {
            val direction = if (buttonFaded) -1 else 1
            binding.fadeButton.animate().translationYBy((direction * 136).px)
                .rotationBy(180.0f).setDuration(animationsDuration).start()
            binding.shareButton.animate().translationYBy((direction * 136).px)
                .alpha(if (buttonFaded) 1.0f else 0.5f).setDuration(animationsDuration).start()
            binding.favoriteButton.animate().translationYBy((direction * 68).px)
                .alpha(if (buttonFaded) 1.0f else 0.5f).setDuration(animationsDuration).start()
            buttonFaded = !buttonFaded
        }
    }
}

object ImageDiffItemCallback : DiffUtil.ItemCallback<CatImage>() {
    override fun areItemsTheSame(oldItem: CatImage, newItem: CatImage) = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: CatImage, newItem: CatImage) =
        oldItem.url == newItem.url
}

val Int.px: Float
    get() = (this * Resources.getSystem().displayMetrics.density)

