package com.example.imageviewer.view.composeview

import android.content.res.Configuration
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.paging.compose.LazyPagingItems
import com.example.imageviewer.domain.CatImage
import com.example.imageviewer.view.composeview.components.OpenedImage
import com.example.imageviewer.view.composeview.values.Default
import com.example.imageviewer.view.composeview.values.LEFT
import com.example.imageviewer.view.composeview.values.RIGHT
import com.example.imageviewer.view.ui.theme.ImageViewerTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    images: List<CatImage> = Default.PREVIEW_CAT_IMAGES,
    buttonListener: ((String) -> Unit)? = null
) {
    val scope = rememberCoroutineScope { Dispatchers.Main }
    val scrollState = rememberLazyListState()
    BoxWithConstraints(modifier.fillMaxSize()) {
        val screen = this

        LazyRow(
            userScrollEnabled = false,
            state = scrollState
        ) {
            items(images) { catImage ->
                OpenedImage(
                    modifier = Modifier
                        .width(screen.maxWidth)
                        .height(screen.maxHeight),
                    buttonListener = {
                        if (!isScrollInit(
                                it,
                                images,
                                scrollState,
                                scope
                            )
                        ) buttonListener?.invoke(it)
                    },
                    image = catImage
                )
            }
        }
    }
}

@Preview("Dark HomeScreen", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview("HomeScreen")
@Composable
fun HomeScreenPreview() {
    val scope = rememberCoroutineScope { Dispatchers.Main }

    val scrollState = rememberLazyListState()
    val onClick: (String) -> Unit = { key ->
        val nextIndex = when (key) {
            LEFT -> {
                val index = scrollState.firstVisibleItemIndex - 1
                if (index < 0) -1
                else index
            }
            RIGHT -> {
                val index = scrollState.firstVisibleItemIndex + 1
                if (index > 10) -1
                else index
            }
            else -> -1
        }
        if (nextIndex >= 0) {
            scope.launch {
                scrollState.animateScrollToItem(nextIndex)
            }
        }
    }


    ImageViewerTheme {
        HomeScreen(buttonListener = onClick)
    }
}

fun isScrollInit(
    key: String,
    images: List<*>,
    state: LazyListState,
    scope: CoroutineScope
): Boolean {
    val nextIndex = when (key) {
        LEFT -> {
            val index = state.firstVisibleItemIndex - 1
            if (index < 0) return false
            else index
        }
        RIGHT -> {
            val index = state.firstVisibleItemIndex + 1
            if (index > images.lastIndex) return false
            else index
        }
        else -> return false
    }
    if (nextIndex >= 0) {
        scope.launch {
            state.animateScrollToItem(nextIndex)
        }
        return true
    }
    return false
}