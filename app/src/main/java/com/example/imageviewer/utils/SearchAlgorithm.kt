package com.example.imageviewer.utils

import com.example.imageviewer.domain.Breed
import com.example.imageviewer.domain.Category
import kotlinx.coroutines.flow.StateFlow

interface SearchAlgorithm {
    fun getBreedsFrom(query: String?): List<Breed>?
    fun getCategoriesFrom(query: String?): List<Category>?
}

class SearchAlgorithmImpl(
    private val breeds: StateFlow<List<Breed>?>,
    private val categories: StateFlow<List<Category>?>
) : SearchAlgorithm {

    private var lastQuery: String? = null
    private var tags: List<String>? = null

    private fun getBreedsFromTags(tags: List<String>?): List<Breed>? {
        if (tags == null) return null
        val result: List<Breed>? = breeds.value?.filter { breed ->
            tags.any { tag ->
                if (breed.name == null) return@any false
                breed.name!!.contains(tag, true)
                        || tag.contains(breed.name!!, true)
            }
        } ?: breeds.value?.filter { breed ->
            tags.any { tag ->
                if (breed.altNames == null) return@any false
                breed.altNames!!.contains(tag, true)
                        || tag.contains(breed.altNames!!, true)
            }
        } ?: breeds.value?.filter { breed ->
            tags.any { tag ->
                if (breed.temperament == null) return@any false
                breed.temperament!!.contains(tag, true)
                        || tag.contains(breed.temperament!!, true)
            }
        }
        return result
    }

    private fun getCategoriesFromTags(tags: List<String>?): List<Category>? {
        if (tags == null) return null
        return categories.value?.filter { category ->
            tags.any { tag ->
                (category.name?.contains(tag, true) ?: false)
                        || if (category.name == null) false else tag.contains(category.name!!, true)
            }
        }
    }

    private fun getTagsFromQuery(query: String?): List<String>? {
        if (query.isNullOrEmpty()) return null
        var substringsStartIndex = 0
        val tags = mutableListOf<String>()
        query.toCharArray().forEachIndexed { index, c ->
            if (c == ' ' || c == ',' || c == '.' || c == '/') {
                if (substringsStartIndex < index) {
                    tags.add(query.substring(substringsStartIndex, index).lowercase())
                }
                substringsStartIndex = index + 1
            }
            if (index == query.lastIndex) {
                tags.add(query.substring(substringsStartIndex, index + 1).lowercase())
            }
        }
        return tags
    }

    override fun getBreedsFrom(query: String?): List<Breed>? {
        if (query == null) return null
        if (query != this.lastQuery) {
            this.lastQuery = query
            this.tags = getTagsFromQuery(query)
        }
        return getBreedsFromTags(this.tags)
    }

    override fun getCategoriesFrom(query: String?): List<Category>? {
        if (query == null) return null
        if (query != this.lastQuery) {
            this.lastQuery = query
            this.tags = getTagsFromQuery(query)
        }
        return getCategoriesFromTags(this.tags)
    }

}