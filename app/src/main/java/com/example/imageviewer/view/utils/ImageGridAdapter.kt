package com.example.imageviewer.view.utils

import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.imageviewer.databinding.ImageRecyclerItemBinding
import com.example.imageviewer.domain.CatImage

class ImageGridAdapter(private inline val openImage: ((position: Int) -> Unit)? = null) :
    PagingDataAdapter<CatImage, ImageGridAdapter.ImageHolder>(ImageDiffItemCallback) {

    override fun onBindViewHolder(holder: ImageHolder, position: Int) {
        holder.onBind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ImageRecyclerItemBinding.inflate(inflater, parent, false)
        val holder = ImageHolder(binding)
        holder.binding.root.setOnClickListener {
            openImage?.invoke(holder.absoluteAdapterPosition)
        }
        return holder
    }

    fun findPosition(image: CatImage?): Int {
        if (image == null) return 0
        for (i in 0 until itemCount) {
            Log.i("VVV", getItem(i).toString())
            if (getItem(i)?.id == image.id) return i
        }
        return 0
    }

    inner class ImageHolder(val binding: ImageRecyclerItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(image: CatImage?) {
            Glide.with(binding.root)
                .load(Uri.parse(image?.url))
                .into(binding.image)
            binding.watchedImage.visibility = if (image?.watched?:0 > 0) View.VISIBLE else View.GONE
            binding.likedImage.visibility = if (image?.liked?:0 > 0) View.VISIBLE else View.GONE
            binding.favoriteImage.visibility = if (image?.favorite?:0 > 0) View.VISIBLE else View.GONE
            binding.alarmImage.visibility = if (image?.alarmTime?:0 > 0) View.VISIBLE else View.GONE
        }
    }
}