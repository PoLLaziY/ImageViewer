package com.example.imageviewer.view.composeview.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.imageviewer.domain.CatImage
import com.example.imageviewer.view.composeview.values.Default
import com.example.imageviewer.view.ui.theme.ImageViewerTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun GridWithOpenableItems(
    modifier: Modifier = Modifier,
    images: List<CatImage?> = Default.PREVIEW_CAT_IMAGES,
    controlButtonListener: ((String, Int) -> Unit)? = null,
    mainScope: CoroutineScope = rememberCoroutineScope { Dispatchers.Main },
    spanCount: Int = 1,
    gridState: LazyGridState = rememberLazyGridState(),
    pagerState: LazyListState = rememberLazyListState(),
    openedPosition: MutableState<Int> = remember { mutableStateOf(-1) },
    appBar: (@Composable () -> Unit)? = null
) {
    var openedPositionVariable: Int by openedPosition

    Box(modifier = modifier) {
        Column(modifier = Modifier.fillMaxSize()) {
            appBar?.invoke()
            ImageGrid(
                scrollState = gridState,
                images = images,
                modifier = modifier.weight(1f),
                spanCount = spanCount,
                onClick = {
                    mainScope.launch {
                        pagerState.scrollToItem(it)
                    }
                    openedPositionVariable = it
                })
        }
        if (openedPositionVariable >= 0) {
            ImagePager(
                modifier = Modifier.fillMaxSize(),
                images = images,
                controlButtonListener = controlButtonListener,
                scrollState = pagerState,
                onClose = {
                    mainScope.launch {
                        gridState.scrollToItem(it)
                    }
                    openedPositionVariable = -1
                },
                mainScope = mainScope
            )
        }
    }
}

@Preview(showBackground = true)
@Preview(uiMode = UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun GridWithOpenableItemsPreview() {
    ImageViewerTheme {
        Surface {
            GridWithOpenableItems(modifier = Modifier.fillMaxSize())
        }
    }
}
