package com.example.imageviewer.view.composeview.components

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import com.example.imageviewer.R
import com.example.imageviewer.view.composeview.values.CANCEL
import com.example.imageviewer.view.composeview.values.FIRST_PLAN_ELEVATION
import com.example.imageviewer.view.composeview.values.SEARCH
import com.example.imageviewer.view.ui.theme.ImageViewerTheme

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    query: String? = null,
    onTextChanged: ((String) -> Unit)? = null
) {
    val keyboard = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    var focus by remember { mutableStateOf(false) }

    Card(modifier = modifier, elevation = FIRST_PLAN_ELEVATION) {
        OutlinedTextField(modifier = modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .onFocusChanged {
                focus = it.isFocused
                if (!focus) keyboard?.hide()
            },
            value = query ?: "", onValueChange = { onTextChanged?.invoke(it) },
            leadingIcon = {
                if (focus) {
                    Icon(
                        modifier = Modifier.clickable {
                            focusManager.clearFocus()
                        },
                        painter = painterResource(id = R.drawable.ic_baseline_cancel_24),
                        contentDescription = CANCEL
                    )
                } else {
                    Icon(
                        painterResource(id = R.drawable.ic_baseline_search_24),
                        contentDescription = SEARCH
                    )
                }
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = { focusManager.clearFocus() }
            )
        )
    }
}

@Preview("Dark SearchBar", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview("SearchBar")
@Composable
fun PreviewSearchBar() {
    var query by remember { mutableStateOf("") }
    ImageViewerTheme {
        SearchBar(query = query) { query = it }
    }
}