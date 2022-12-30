package com.example.imageviewer.view.composeview.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.imageviewer.domain.CatImage
import com.example.imageviewer.view.composeview.values.Default
import com.example.imageviewer.view.composeview.values.LEFT
import com.example.imageviewer.view.composeview.values.RIGHT
import com.example.imageviewer.view.ui.theme.ImageViewerTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun ImagePager(
    modifier: Modifier = Modifier,
    images: List<CatImage?> = Default.PREVIEW_CAT_IMAGES,
    buttonListener: ((String, Int) -> Unit)? = null
) {
    val scope = rememberCoroutineScope { Dispatchers.Main }
    val scrollState = rememberLazyListState()
    BoxWithConstraints(modifier.fillMaxSize()) {
        val screen = this

        LazyRow(
            userScrollEnabled = false,
            state = scrollState
        ) {
            itemsIndexed(images) { index, catImage ->
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
                        ) buttonListener?.invoke(it, index)
                    },
                    image = catImage
                )
            }
        }
    }
}

@Preview("Dark ImagePager", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview("ImagePager")
@Composable
fun ImagePagerPreview() {
    ImageViewerTheme {
        ImagePager()
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