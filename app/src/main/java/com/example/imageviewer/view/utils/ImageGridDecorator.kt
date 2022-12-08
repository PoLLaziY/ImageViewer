package com.example.imageviewer.view.utils

import android.content.Context
import android.graphics.Rect
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.core.view.doOnAttach
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.imageviewer.R
import java.lang.Exception

class ImageGridDecorator(context: Context) : RecyclerView.ItemDecoration() {
    private val itemMargin = context.resources.getDimension(R.dimen.grid_item_margin).toInt()

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        recycler: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = recycler.getChildAdapterPosition(view)
        val spanCount = if (recycler.layoutManager is GridLayoutManager) {
            (recycler.layoutManager as GridLayoutManager).spanCount
        } else 1

        val top = if (position < spanCount) itemMargin else 0

        val bottom = itemMargin

        val right = when (position%spanCount) {
            0 -> itemMargin/3
            spanCount - 1 -> itemMargin
            else -> itemMargin/3*2
        }

        val left = when (position%spanCount) {
            0 -> itemMargin
            spanCount - 1 -> itemMargin/3
            else -> itemMargin/3*2
        }

        outRect.set(left, top, right, bottom)
    }
}