package com.example.imageviewer.view

import android.content.res.Resources
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.imageviewer.databinding.OpenedImageBinding
import com.example.imageviewer.domain.CatImage

class ImagePagerAdapter(private val recycler: RecyclerView) :
    RecyclerView.Adapter<ImagePagerAdapter.ImageHolder>() {

    inner class ImageHolder(val binding: OpenedImageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private var buttonFaded = true

        fun onBind(catImage: CatImage?) {
            if (catImage == null) return
            Glide.with(binding.root)
                .load(Uri.parse(catImage.url))
                .into(binding.image)

            if (!buttonFaded) {
                binding.fadeButton.translationY += 136.px
                binding.fadeButton.rotation += 180
                binding.shareButton.translationY += 136.px
                binding.favoriteButton.translationY += 68.px
                buttonFaded = !buttonFaded
            }
        }

        fun onFadeButtonClick() {
            val animationDuration = 500L
            val direction = if (buttonFaded) -1 else 1
            binding.fadeButton.animate().translationYBy((direction * 136).px)
                .rotationBy(180.0f).setDuration(animationDuration).start()
            binding.shareButton.animate().translationYBy((direction * 136).px)
                .alpha(if (buttonFaded) 1.0f else 0.5f).setDuration(animationDuration).start()
            binding.favoriteButton.animate().translationYBy((direction * 68).px)
                .alpha(if (buttonFaded) 1.0f else 0.5f).setDuration(animationDuration).start()
            buttonFaded = !buttonFaded
        }
    }

    var list: List<CatImage>? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = OpenedImageBinding.inflate(inflater, parent, false)
        val holder = ImageHolder(binding)
        binding.nextButton.setOnClickListener {
            if (holder.adapterPosition >= (list?.size ?: 0) - 1) return@setOnClickListener
            recycler.smoothScrollToPosition(holder.adapterPosition + 1)
        }
        binding.previousButton.setOnClickListener {
            if (holder.adapterPosition <= 0) return@setOnClickListener
            recycler.smoothScrollToPosition(holder.adapterPosition - 1)
        }
        binding.fadeButton.setOnClickListener {
            holder.onFadeButtonClick()
        }
        return holder
    }

    override fun onBindViewHolder(holder: ImageHolder, position: Int) {
        holder.onBind(list?.get(position))
    }

    override fun getItemCount(): Int {
        return list?.size ?: 0
    }
}

val Int.px: Float
    get() = (this * Resources.getSystem().displayMetrics.density)

