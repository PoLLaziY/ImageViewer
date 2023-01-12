package com.example.imageviewer.view.components.composition

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.imageviewer.domain.CatImage
import com.example.imageviewer.view.components.ImageGridItem
import com.example.imageviewer.view.ui.theme.ImageViewerTheme
import com.example.imageviewer.view.values.Default
import com.example.imageviewer.view.values.OPENED_IMAGE_PADDING

@Composable
fun ImageGrid(
    modifier: Modifier = Modifier,
    spanCount: Int = 2,
    images: List<CatImage?> = Default.PREVIEW_CAT_IMAGES,
    scrollState: LazyGridState = rememberLazyGridState(),
    onClick: ((Int) -> Unit)? = null
) {
    LazyVerticalGrid(
        state = scrollState,
        modifier = modifier,
        columns = GridCells.Fixed(spanCount),
        horizontalArrangement = Arrangement.spacedBy(OPENED_IMAGE_PADDING),
        verticalArrangement = Arrangement.spacedBy(OPENED_IMAGE_PADDING),
        contentPadding = PaddingValues(OPENED_IMAGE_PADDING)
    ) {

        itemsIndexed(images) { index, item ->
            ImageGridItem(catImage = item, onClick = onClick?.with(index))
        }
    }
}

@Preview("Dark GridItem", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview("GridItem")
@Composable
fun PreviewImageGrid() {
    ImageViewerTheme {
        Surface {
            ImageGrid(
                modifier = Modifier
                    .fillMaxSize()
            )
        }
    }
}