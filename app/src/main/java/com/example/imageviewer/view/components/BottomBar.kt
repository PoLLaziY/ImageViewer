package com.example.imageviewer.view.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.imageviewer.view.values.BottomBarIcon
import com.example.imageviewer.view.values.Buttons.BOTTOM_BAR_ITEMS
import com.example.imageviewer.view.values.FIRST_PLAN_ELEVATION
import com.example.imageviewer.view.values.HOME
import com.example.imageviewer.view.ui.theme.DarkGray
import com.example.imageviewer.view.ui.theme.ImageViewerTheme
import com.example.imageviewer.view.ui.theme.Orange
import com.example.imageviewer.view.ui.theme.White


@Composable
fun BottomBar(
    modifier: Modifier = Modifier,
    selectedItem: String? = null,
    onClick: ((String) -> Unit)? = null,
    items: List<BottomBarIcon> = BOTTOM_BAR_ITEMS
) {
    Card(
        modifier = modifier,
        elevation = FIRST_PLAN_ELEVATION,
        shape = MaterialTheme.shapes.medium
    ) {
        BottomNavigation(backgroundColor = MaterialTheme.colors.primary, contentColor = DarkGray) {
            items.forEach { item ->
                BottomNavigationItem(
                    selected = item.label == selectedItem,
                    icon = { Icon(painterResource(id = item.icon), item.label) },
                    label = { Text(text = item.label) },
                    onClick = { onClick?.invoke(item.label) },
                    selectedContentColor = Orange,
                    unselectedContentColor = White
                )
            }
        }
    }
}

@Preview("Dark BottomBar", uiMode = UI_MODE_NIGHT_YES, showBackground = true)
@Preview("BottomBar", showBackground = true)
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