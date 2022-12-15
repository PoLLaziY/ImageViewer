package com.example.imageviewer.view.utils

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.imageviewer.databinding.BreedItemBinding
import com.example.imageviewer.domain.Breed

class BreedListAdapter : RecyclerView.Adapter<BreedListAdapter.BreedHolder>() {

    var list: List<Breed>? = null
        set(value) {
            if (field == value) return
            field = value
            notifyDataSetChanged()
        }

    inner class BreedHolder(private val binding: BreedItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(breed: Breed?) {
            if (breed == null) return
            binding.breedName.text = breed.name
            binding.altName.text = breed.altNames
            binding.lifeSpan.text = breed.lifeSpan
            binding.temperament.text = breed.temperament
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BreedHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = BreedItemBinding.inflate(inflater, parent, false)
        return BreedHolder(binding)
    }

    override fun onBindViewHolder(holder: BreedHolder, position: Int) {
        holder.bind(list?.getOrNull(position))
    }

    override fun getItemCount(): Int {
        return list?.size ?: 0
    }
}

object BreedDiffCallback : DiffUtil.ItemCallback<Breed>() {
    override fun areItemsTheSame(oldItem: Breed, newItem: Breed): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Breed, newItem: Breed): Boolean {
        return oldItem.name == newItem.name && oldItem.altNames == newItem.altNames
                && oldItem.lifeSpan == newItem.lifeSpan && oldItem.temperament == newItem.temperament
    }
}