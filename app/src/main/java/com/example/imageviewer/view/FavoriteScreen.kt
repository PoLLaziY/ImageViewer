package com.example.imageviewer.view

import android.content.res.Configuration
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.imageviewer.view.components.composition.GridWithOpenableItems
import com.example.imageviewer.view.ui.theme.ImageViewerTheme
import com.example.imageviewer.viewModel.FavoriteScreenViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

@Composable
fun FavoriteScreen(
    modifier: Modifier = Modifier,
    favoriteViewModel: FavoriteScreenViewModel? = viewModel(),
    mainScope: CoroutineScope = rememberCoroutineScope { Dispatchers.Main }
) {
    val viewModel = favoriteViewModel?: viewModel()
    val pagerData = viewModel.images.collectAsLazyPagingItems()
    val list = pagerData.toAbstractList()
    val context = LocalContext.current

    GridWithOpenableItems(
        modifier = modifier,
        images = list,
        controlButtonListener = { key, position ->
            viewModel.onClick(context, key, list[position])
        },
        mainScope = mainScope,
        spanCount = 3
    )

}

@Preview("Dark FavoriteScreen", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview("FavoriteScreen")
@Composable
fun FavoriteScreenPreview() {
    ImageViewerTheme {
        FavoriteScreen()
    }
}