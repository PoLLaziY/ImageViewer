package com.example.imageviewer.view.composeview.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.imageviewer.view.composeview.values.BottomBarIcon
import com.example.imageviewer.view.composeview.values.Buttons.BOTTOM_BAR_ITEMS
import com.example.imageviewer.view.composeview.values.FIRST_PLAN_ELEVATION
import com.example.imageviewer.view.composeview.values.HOME
import com.example.imageviewer.view.ui.theme.ImageViewerTheme
import com.example.imageviewer.view.ui.theme.Orange


@Composable
fun BottomBar(
    modifier: Modifier = Modifier,
    selectedItem: String? = null,
    onClick: ((String) -> Unit)? = null,
    items: List<BottomBarIcon> = BOTTOM_BAR_ITEMS
) {
    Card(modifier = modifier, elevation = FIRST_PLAN_ELEVATION) {
        BottomNavigation(modifier = modifier) {
            items.forEach { item ->
                BottomNavigationItem(
                    selected = item.label == selectedItem,
                    icon = { Icon(painterResource(id = item.icon), item.label) },
                    label = { Text(text = item.label) },
                    onClick = { onClick?.invoke(item.label) },
                    selectedContentColor = Orange,
                    unselectedContentColor = MaterialTheme.colors.onPrimary
                )
            }
        }
    }
}

@Preview("Dark BottomBar", uiMode = UI_MODE_NIGHT_YES)
@Preview("BottomBar")
@Composable
fun PreviewBottomBar() {
    var selectedItem by remember { mutableStateOf(HOME) }
    ImageViewerTheme() {
        BottomBar(selectedItem = selectedItem,
            onClick = {
                selectedItem = it
            })
    }
}