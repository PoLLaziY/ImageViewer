package com.example.imageviewer.view

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.imageviewer.R

class ImageGridDecorator(context: Context) : RecyclerView.ItemDecoration() {
    private val itemMargin = context.resources.getDimension(R.dimen.grid_item_margin).toInt()

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        recycler: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = recycler.getChildLayoutPosition(view)
        val spanCount = if (recycler.layoutManager is GridLayoutManager) {
            (recycler.layoutManager as GridLayoutManager).spanCount
        } else 1

        val top =
            if (position < spanCount) itemMargin else 0

        val bottom = itemMargin

        val right = if ((position + 1) % spanCount == 0) itemMargin else itemMargin / 2

        val left = if ((position + 1) % spanCount == 1) itemMargin else itemMargin / 2

        outRect.set(left, top, right, bottom)
    }
}