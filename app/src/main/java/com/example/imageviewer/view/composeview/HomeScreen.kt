package com.example.imageviewer.view.composeview

import android.content.res.Configuration
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
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
import com.example.imageviewer.view.composeview.components.ImagePager
import com.example.imageviewer.view.composeview.values.LOAD_ERROR
import com.example.imageviewer.view.composeview.values.PROGRESS_BAR_STROKE_WIGHT
import com.example.imageviewer.view.ui.theme.ImageViewerTheme
import com.example.imageviewer.view.ui.theme.Orange
import com.example.imageviewer.viewModel.HomeScreenViewModel

@Composable
fun HomeScreen(modifier: Modifier = Modifier, viewModel: HomeScreenViewModel = viewModel()) {
    val imagesPagingData = viewModel.images.collectAsLazyPagingItems()
    val context = LocalContext.current
    when (imagesPagingData.loadState.refresh) {
        LoadState.Loading -> {
            LoadingProgressBar(modifier)
        }
        is LoadState.Error -> {
            ErrorHolder(modifier)
        }
        else -> {
            val list = imagesPagingData.toAbstractList()
            ImagePager(
                modifier = modifier,
                images = list,
                controlButtonListener = { key, index -> viewModel.onClick(context, key, list[index]) }
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
fun ErrorHolder(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize()) {
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
                color = MaterialTheme.colors.onPrimary
            )
        }
    }
}

@Preview("Dark ErrorHolder", uiMode = UI_MODE_NIGHT_YES)
@Preview("ErrorHolder")
@Composable
fun ErrorHolderPreview() {
    ImageViewerTheme {
        ErrorHolder()
    }
}

@Preview("Dark HomeScreen", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview("HomeScreen")
@Composable
fun HomeScreenPreview() {
    ImageViewerTheme {
        HomeScreen()
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