package com.example.imageviewer.view.composeview

import androidx.compose.foundation.layout.Column
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.imageviewer.view.ui.theme.ImageViewerTheme
import com.example.imageviewer.view.ui.theme.Orange

data class BottomBarItem(val label: String, val icon: ImageVector)

val BOTTOM_BAR_ITEMS = listOf(
    BottomBarItem(
        label = "Home",
        icon = Icons.Filled.Home
    ),
    BottomBarItem(
        label = "Search",
        icon = Icons.Filled.Search
    ),
    BottomBarItem(
        label = "Favorite",
        icon = Icons.Filled.Favorite
    )
)

@Composable
fun BottomBar(
    bottomNavigationState: MutableLiveData<Int> = MutableLiveData(0),
    items: List<BottomBarItem> = BOTTOM_BAR_ITEMS
) {
    val selectedItem by bottomNavigationState.observeAsState()

    BottomNavigation() {
        items.forEachIndexed { index, item ->
            BottomNavigationItem(
                selected = index == selectedItem,
                icon = { Icon(item.icon, item.label) },
                label = { Text(text = item.label) },
                onClick = { bottomNavigationState.postValue(index) },
                selectedContentColor = Orange,
                unselectedContentColor = MaterialTheme.colors.onPrimary
            )
        }
    }
}

@Preview
@Composable
fun PreviewBottomBar() {
    Column() {
        ImageViewerTheme(darkTheme = true) {
            Column {
                BottomBar()
                Surface {
                    BottomBar()
                }
            }
        }
        ImageViewerTheme() {
            Column {
                BottomBar()
                Surface {
                    BottomBar()
                }
            }
        }
    }
}