package com.example.imageviewer.view

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.imageviewer.R
import com.example.imageviewer.domain.CatImage
import com.example.imageviewer.view.components.composition.ImagePager
import com.example.imageviewer.view.ui.theme.ImageViewerTheme
import com.example.imageviewer.view.ui.theme.Orange
import com.example.imageviewer.view.values.LOAD_ERROR
import com.example.imageviewer.view.values.PROGRESS_BAR_STROKE_WIGHT
import com.example.imageviewer.viewModel.HomeScreenViewModel

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    homeScreenViewModel: HomeScreenViewModel? = viewModel()
) {
    val viewModel: HomeScreenViewModel = homeScreenViewModel ?: viewModel()
    val imagesPagingData = viewModel.images.collectAsLazyPagingItems()
    val context = LocalContext.current

    when (imagesPagingData.loadState.refresh) {
        LoadState.Loading -> {
            LoadingProgressBar(modifier)
        }
        is LoadState.Error -> {
            ErrorHolder(modifier) {
                imagesPagingData.refresh()
            }
        }
        else -> {
            val list = imagesPagingData.toAbstractList()
            ImagePager(
                modifier = modifier,
                images = list,
                controlButtonListener = { key, index ->
                    viewModel.onClick(
                        context,
                        key,
                        list[index]
                    )
                }
            )
        }
    }
}

@Composable
fun LoadingProgressBar(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize()) {
        Box(
            modifier = modifier
                .aspectRatio(1f)
                .fillMaxSize()
                .align(Alignment.Center)
        ) {
            CircularProgressIndicator(
                modifier = modifier
                    .fillMaxSize(0.6f)
                    .align(Alignment.Center),
                color = Orange,
                strokeWidth = PROGRESS_BAR_STROKE_WIGHT
            )
        }

    }
}

@Composable
fun ErrorHolder(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .clickable(onClick = onClick)
    ) {
        Column(
            modifier = Modifier
                .wrapContentSize()
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .aspectRatio(1f)
            ) {
                Image(
                    modifier = Modifier
                        .fillMaxSize(0.6f)
                        .align(Alignment.Center),
                    painter = painterResource(id = R.drawable.ic_baseline_cancel_24),
                    contentDescription = LOAD_ERROR
                )
            }
            Text(
                text = LOAD_ERROR,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.h3,
                color = MaterialTheme.colors.onBackground
            )
        }
    }
}

@Preview("Dark ErrorHolder", uiMode = UI_MODE_NIGHT_YES, showBackground = true)
@Preview("ErrorHolder", showBackground = true)
@Composable
fun ErrorHolderPreview() {
    ImageViewerTheme {
        Surface {
            ErrorHolder()
        }
    }
}

@Preview("Dark HomeScreen", uiMode = UI_MODE_NIGHT_YES)
@Preview("HomeScreen", showBackground = true)
@Composable
fun HomeScreenPreview() {
    ImageViewerTheme {
        Surface {
            HomeScreen()
        }
    }
}


fun LazyPagingItems<CatImage>.toAbstractList(): List<CatImage?> {
    val pagingItem = this
    return object : AbstractList<CatImage?>() {
        override val size: Int = pagingItem.itemCount

        override fun get(index: Int): CatImage? {
            return pagingItem[index]
        }

    }
}