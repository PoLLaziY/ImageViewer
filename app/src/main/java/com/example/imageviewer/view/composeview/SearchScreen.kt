package com.example.imageviewer.view.composeview

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.imageviewer.view.composeview.components.GridWithOpenableItems
import com.example.imageviewer.view.composeview.components.SearchBar
import com.example.imageviewer.view.composeview.values.FIRST_PLAN_ELEVATION
import com.example.imageviewer.view.composeview.values.OPENED_IMAGE_BUTTON_MARGIN
import com.example.imageviewer.view.ui.theme.ImageViewerTheme
import com.example.imageviewer.viewModel.SearchScreenViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    searchViewModel: SearchScreenViewModel? = viewModel(),
    mainScope: CoroutineScope = rememberCoroutineScope { Dispatchers.Main }
) {
    val viewModel = searchViewModel?: viewModel()
    val pagerData = viewModel.images.collectAsLazyPagingItems()
    val list = pagerData.toAbstractList()
    val context = LocalContext.current
    val innerVM = remember { viewModel }
    val query = innerVM.query

    Column(modifier = Modifier.fillMaxSize()) {

        GridWithOpenableItems(
            modifier = modifier
                .weight(1f)
                .fillMaxWidth(),
            images = list,
            controlButtonListener = { key, position ->
                viewModel.onClick(context, key, list[position])
            },
            mainScope = mainScope,
            spanCount = 1,
            appBar = {
                Card(
                    elevation = FIRST_PLAN_ELEVATION,
                    shape = RoundedCornerShape(0f),
                    backgroundColor = MaterialTheme.colors.primary
                ) {
                    SearchBar(Modifier.padding(OPENED_IMAGE_BUTTON_MARGIN), query = query, onTextChanged = { viewModel.query = it })
                }
            }
        )
    }

}

@Preview("Dark SearchScreen", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview("SearchScreen")
@Composable
fun SearchScreenPreview() {
    ImageViewerTheme {
        Surface {
            SearchScreen()
        }
    }
}