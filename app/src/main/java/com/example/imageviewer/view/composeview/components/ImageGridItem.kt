package com.example.imageviewer.view.composeview.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.annotation.Dimension
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.imageviewer.R
import com.example.imageviewer.domain.CatImage
import com.example.imageviewer.view.composeview.values.*
import com.example.imageviewer.view.ui.theme.ImageViewerTheme

const val GRID_ITEM_ICON_PADDING_RATIO = 0.05f
const val GRID_ITEM_ICON_SIZE_RATIO = 0.2f

@Composable
fun ImageGridItem(
    modifier: Modifier = Modifier, catImage: CatImage? = Default.PREVIEW_CAT_IMAGE,
    imageStateIcons: List<ImageStateIcon> = Buttons.IMAGE_STATE_ICONS,
    onClick: (() -> Unit)? = null,
    shape: Shape = MaterialTheme.shapes.large
) {
    Card(
        modifier = modifier
            .fillMaxSize()
            .aspectRatio(1.0f),
        shape = shape,
        elevation = CONTENT_PLAN_ELEVATION
    ) {
        Box(
            Modifier
                .background(MaterialTheme.colors.surface)
                .clickable { onClick?.invoke() }) {
            Image(
                modifier = Modifier.fillMaxSize(),
                painter = rememberAsyncImagePainter(
                    model = catImage?.url,
                    placeholder = painterResource(id = R.drawable.ic_launcher_background)
                ),
                contentDescription = CAT_IMAGE,
                contentScale = ContentScale.Crop
            )

            val padding = remember {
                1.0f - GRID_ITEM_ICON_PADDING_RATIO
            }

            Box(
                modifier = Modifier
                    .fillMaxSize(padding)
                    .align(Alignment.Center)
            ) {
                imageStateIcons.forEach {
                    if (it.enabler(catImage)) {
                        ImageStateIcon(modifier = Modifier.align(it.alignment), icon = it)
                    }
                }
            }


        }
    }
}

@Composable
fun ImageStateIcon(modifier: Modifier = Modifier, icon: ImageStateIcon) {

    val iconSize = remember {
        GRID_ITEM_ICON_SIZE_RATIO / (1.0f - GRID_ITEM_ICON_PADDING_RATIO)
    }
    Image(
        modifier = modifier
            .fillMaxSize(iconSize),
        painter = painterResource(id = icon.icon),
        contentDescription = icon.name
    )
}

@Preview("Dark GridItem", uiMode = UI_MODE_NIGHT_YES)
@Preview("GridItem")
@Composable
fun PreviewImageGridItem() {
    ImageViewerTheme {
        ImageGridItem(
            modifier = Modifier
                .size(200.dp)
                .padding(8.dp)
        )
    }
}