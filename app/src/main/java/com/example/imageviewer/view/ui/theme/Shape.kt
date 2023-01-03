package com.example.imageviewer.view.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Shapes
import androidx.compose.ui.unit.dp
import com.example.imageviewer.view.composeview.values.LARGE_SHAPE_ROUND_CORNER
import com.example.imageviewer.view.composeview.values.MEDIUM_SHAPE_ROUND_CORNED
import com.example.imageviewer.view.composeview.values.SMALL_SHAPE_ROUND_CORNED

val Shapes = Shapes(
    small = RoundedCornerShape(SMALL_SHAPE_ROUND_CORNED),
    medium = RoundedCornerShape(MEDIUM_SHAPE_ROUND_CORNED),
    large = RoundedCornerShape(LARGE_SHAPE_ROUND_CORNER)
)