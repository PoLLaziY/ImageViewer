package com.example.imageviewer.view

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ImagePagerLayoutManager(context: Context): LinearLayoutManager(context) {

    private var scrollListenerAdded = false
    private var scrollToNext = true

    init {
        orientation = HORIZONTAL
    }

    private val scrollListener by lazy {
        object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == 1 && recyclerView.layoutManager is LinearLayoutManager) {
                    val position = if (scrollToNext) {
                        (recyclerView.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                    } else {
                        (recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                    }
                    recyclerView.scrollToPosition(position)
                }
            }
        }
    }

    override fun canScrollHorizontally(): Boolean {
        return isSmoothScrolling
    }

    override fun smoothScrollToPosition(
        recyclerView: RecyclerView?,
        state: RecyclerView.State?,
        position: Int
    ) {
        if (!scrollListenerAdded && recyclerView != null) {
            recyclerView.addOnScrollListener(scrollListener)
            scrollListenerAdded = true
        }
        scrollToNext = findFirstVisibleItemPosition() <= position
        super.smoothScrollToPosition(recyclerView, state, position)
    }
}