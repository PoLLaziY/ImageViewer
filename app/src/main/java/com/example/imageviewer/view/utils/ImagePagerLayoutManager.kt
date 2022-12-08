package com.example.imageviewer.view.utils

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ImagePagerLayoutManager(context: Context) :
    LinearLayoutManager(context) {

    private var scrollToNext = true

    private val scrollListener by lazy {
        object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == 1 && recyclerView.layoutManager is LinearLayoutManager) {
                    val position = if (scrollToNext) {
                        findLastVisibleItemPosition()
                    } else {
                        findFirstVisibleItemPosition()
                    }
                    recyclerView.scrollToPosition(position)
                }
            }
        }
    }

    override fun onAttachedToWindow(recyclerView: RecyclerView?) {
        orientation = HORIZONTAL
        super.onAttachedToWindow(recyclerView)
        recyclerView?.addOnScrollListener(scrollListener)
    }

    override fun onDetachedFromWindow(view: RecyclerView?, recycler: RecyclerView.Recycler?) {
        super.onDetachedFromWindow(view, recycler)
        view?.removeOnScrollListener(scrollListener)
    }

    override fun canScrollHorizontally(): Boolean {
        return isSmoothScrolling
    }

    override fun smoothScrollToPosition(
        recyclerView: RecyclerView?,
        state: RecyclerView.State?,
        position: Int
    ) {
        scrollToNext = findFirstVisibleItemPosition() <= position
        super.smoothScrollToPosition(recyclerView, state, position)
    }
}