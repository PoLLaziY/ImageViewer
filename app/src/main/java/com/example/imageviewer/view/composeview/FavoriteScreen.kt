package com.example.imageviewer.view.composeview

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.imageviewer.domain.CatImage
import com.example.imageviewer.view.composeview.components.GridWithOpenableItems
import com.example.imageviewer.view.composeview.components.ImageGrid
import com.example.imageviewer.view.composeview.components.ImagePager
import com.example.imageviewer.view.composeview.components.SearchBar
import com.example.imageviewer.view.composeview.values.Default
import com.example.imageviewer.view.ui.theme.ImageViewerTheme
import com.example.imageviewer.viewModel.FavoriteScreenViewModel
import com.example.imageviewer.viewModel.SearchScreenViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun FavoriteScreen(
    modifier: Modifier = Modifier,
    viewModel: FavoriteScreenViewModel = viewModel(),
    mainScope: CoroutineScope = rememberCoroutineScope { Dispatchers.Main }
) {
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