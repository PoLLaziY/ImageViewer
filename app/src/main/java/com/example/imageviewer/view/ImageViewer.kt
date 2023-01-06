package com.example.imageviewer.view

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.imageviewer.di.AppComponent
import com.example.imageviewer.view.components.BottomBar
import com.example.imageviewer.view.components.SearchScreen
import com.example.imageviewer.view.ui.theme.ImageViewerTheme
import com.example.imageviewer.view.values.HOME
import com.example.imageviewer.view.values.SEARCH
import com.example.imageviewer.view.values.THEME

@Composable
@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
fun ImageViewer(appComponent: AppComponent? = null) {

    var navigationState by remember { mutableStateOf(HOME) }
    var isThemeDark by remember { mutableStateOf(false) }

    ImageViewerTheme(darkTheme = isThemeDark) {
        Surface(color = MaterialTheme.colors.surface) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
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
                    onClick = {
                        if (it === THEME) {
                            isThemeDark = !isThemeDark
                        } else navigationState = it
                    })
            }
        }
    }
}