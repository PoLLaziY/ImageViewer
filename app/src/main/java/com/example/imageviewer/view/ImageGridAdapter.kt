package com.example.imageviewer.view

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.imageviewer.databinding.ImageRecyclerItemBinding
import com.example.imageviewer.domain.CatImage

class ImageGridAdapter(private inline val openImage: (position: Int) -> Unit) :
    PagingDataAdapter<CatImage, ImageGridAdapter.ImageHolder>(ImageDiffItemCallback) {

    override fun onBindViewHolder(holder: ImageHolder, position: Int) {
        holder.onBind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ImageRecyclerItemBinding.inflate(inflater, parent, false)
        val holder = ImageHolder(binding)
        holder.binding.root.setOnClickListener {
            openImage.invoke(holder.absoluteAdapterPosition)
        }
        return holder
    }

    inner class ImageHolder(val binding: ImageRecyclerItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(image: CatImage?) {
            Glide.with(binding.root)
                .load(Uri.parse(image?.url))
                .into(binding.image)
        }
    }
}