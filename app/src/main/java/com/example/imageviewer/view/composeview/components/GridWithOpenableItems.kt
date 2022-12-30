package com.example.imageviewer.view.composeview.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.imageviewer.domain.CatImage
import com.example.imageviewer.view.composeview.values.Default
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun GridWithOpenableItems(
    modifier: Modifier = Modifier,
    images: List<CatImage?> = Default.PREVIEW_CAT_IMAGES,
    onQueryChanged: ((String) -> Unit)? = null,
    controlButtonListener: ((String, Int) -> Unit)? = null,
    mainScope: CoroutineScope = rememberCoroutineScope { Dispatchers.Main },
    spanCount: Int = 1
) {
    var openedPosition: Int by remember { mutableStateOf(-1) }
    val gridState = rememberLazyGridState()
    val pagerState = rememberLazyListState()

    Box(modifier = modifier) {
        Column(modifier = Modifier.fillMaxSize()) {
            if (onQueryChanged != null) {
                var lastQuery by remember {
                    mutableStateOf("")
                }
                SearchBar(query = lastQuery, onTextChanged = {
                    lastQuery = it
                    onQueryChanged.invoke(it)
                })
            }
            ImageGrid(
                scrollState = gridState,
                images = images,
                modifier = Modifier.weight(1f),
                spanCount = spanCount,
                onClick = { openedPosition = it })
        }
        if (openedPosition >= 0) {
            ImagePager(
                modifier = Modifier.fillMaxSize(),
                images = images,
                controlButtonListener = controlButtonListener,
                scrollState = pagerState,
                onClose = {
                    openedPosition = -1
                    mainScope.launch {
                        gridState.scrollToItem(it)
                    }
                },
                mainScope = mainScope
            )
        }
    }

}
