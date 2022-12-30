package com.example.imageviewer.view.composeview

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.imageviewer.view.composeview.components.BottomBar
import com.example.imageviewer.view.composeview.components.ImageGrid
import com.example.imageviewer.view.composeview.values.HOME
import com.example.imageviewer.view.composeview.values.SEARCH
import com.example.imageviewer.view.ui.theme.ImageViewerTheme

@Composable
@Preview
fun ImageViewer() {
    
    var navigationState by remember { mutableStateOf(HOME) }

    ImageViewerTheme {
        Column(modifier = Modifier.fillMaxSize()) {
            Box(modifier = Modifier.weight(1f)) {
                when (navigationState) {
                    HOME -> HomeScreen()
                    SEARCH -> SearchScreen()
                    else -> ImageGrid(spanCount = 3)
                }

            }

            BottomBar(modifier = Modifier, selectedItem = navigationState, onClick = { navigationState = it })
        }
    }
}