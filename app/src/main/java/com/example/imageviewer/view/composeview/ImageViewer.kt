package com.example.imageviewer.view.composeview

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.di.AppComponent
import com.example.imageviewer.view.composeview.components.BottomBar
import com.example.imageviewer.view.composeview.values.HOME
import com.example.imageviewer.view.composeview.values.SEARCH
import com.example.imageviewer.view.ui.theme.Gray
import com.example.imageviewer.view.ui.theme.ImageViewerTheme

@Composable
@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
fun ImageViewer(appComponent: AppComponent? = null) {

    var navigationState by remember { mutableStateOf(HOME) }

    Surface {
        ImageViewerTheme {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Gray)
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    when (navigationState) {
                        HOME -> HomeScreen(homeScreenViewModel = appComponent?.homeScreenViewModel)
                        SEARCH -> SearchScreen(searchViewModel = appComponent?.searchScreenViewModel)
                        else -> FavoriteScreen(favoriteViewModel = appComponent?.favoriteScreenViewModel)
                    }
                }

                BottomBar(
                    modifier = Modifier,
                    selectedItem = navigationState,
                    onClick = { navigationState = it })
            }
        }
    }
}