package com.example.imageviewer.view.composeview

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.imageviewer.view.composeview.components.SearchBar
import com.example.imageviewer.view.ui.theme.ImageViewerTheme

@Composable
fun SearchScreen(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        SearchBar()
        ImageGrid(modifier = Modifier.weight(1f), spanCount = 1)
    }

}

@Preview("Dark SearchScreen", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview("SearchScreen")
@Composable
fun SearchScreenPreview() {
    ImageViewerTheme {
        SearchScreen()
    }
}