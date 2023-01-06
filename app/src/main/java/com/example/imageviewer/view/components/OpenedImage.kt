package com.example.imageviewer.view.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.rememberAsyncImagePainter
import com.example.imageviewer.R
import com.example.imageviewer.domain.CatImage
import com.example.imageviewer.view.ui.theme.ImageViewerTheme
import com.example.imageviewer.view.values.*
import com.example.imageviewer.view.values.Buttons.OPENED_IMAGE_BUTTONS

@Composable
fun OpenedImage(
    modifier: Modifier = Modifier,
    image: CatImage? = Default.PREVIEW_CAT_IMAGE,
    buttonListener: ((id: String) -> Unit)? = null,
    onCloseImage: (() -> Unit)? = null
) {
    Column(
        modifier = modifier
            //.verticalScroll(state = rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(OPENED_IMAGE_PADDING))
        ImageHolder(Modifier.weight(1f), image, onCloseImage)
        Spacer(modifier = Modifier.height(OPENED_IMAGE_PADDING))
        Controller(catImage = image, buttonListener = buttonListener)
        //Spacer(modifier = Modifier.height(OPENED_IMAGE_PADDING))
        //Details()
    }
}

@Composable
fun ImageHolder(modifier: Modifier = Modifier, catImage: CatImage?, onCloseImage: (() -> Unit)?) {
    Card(
        modifier = modifier
            .padding(start = OPENED_IMAGE_PADDING, end = OPENED_IMAGE_PADDING)
            .fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        backgroundColor = MaterialTheme.colors.primary,
        elevation = CONTENT_PLAN_ELEVATION
    ) {
        Box {
            Image(
                modifier = Modifier.fillMaxSize(),
                painter = rememberAsyncImagePainter(
                    catImage?.url,
                    placeholder = painterResource(id = R.drawable.ic_launcher_background)
                ),
                contentDescription = catImage?.url
            )
            if (onCloseImage != null) IconButton(
                modifier = Modifier.align(Alignment.TopCenter),
                onClick = onCloseImage) {
                Icon(
                    modifier = Modifier
                        .size(OPENED_IMAGE_BUTTON_DIMEN)
                        .padding(
                            OPENED_IMAGE_BUTTON_MARGIN
                        ),
                    painter = painterResource(id = R.drawable.ic_baseline_keyboard_arrow_up_24),
                    contentDescription = "",
                    tint = MaterialTheme.colors.onPrimary
                )
            }
        }
    }
}

@Composable
fun Controller(
    buttonList: List<List<ControlPanelButton>> = OPENED_IMAGE_BUTTONS,
    catImage: CatImage?, buttonListener: ((id: String) -> Unit)? = null
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        shape = MaterialTheme.shapes.medium,
        backgroundColor = MaterialTheme.colors.primary,
        elevation = CONTROL_PANEL_PLAN_ELEVATION
    ) {
        Column() {
            Spacer(modifier = Modifier.height(OPENED_IMAGE_BUTTON_MARGIN))
            buttonList.forEach { list ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    list.forEach { item ->
                        IconButton(
                            onClick = { buttonListener?.invoke(item.label) },
                            Modifier.size(OPENED_IMAGE_BUTTON_DIMEN)
                        ) {
                            val enabler = item.enabler?.invoke(catImage)

                                Icon(
                                    modifier = Modifier.size(OPENED_IMAGE_BUTTON_DIMEN),
                                    painter = painterResource(
                                        id = item.icon(enabler)
                                    ),
                                    contentDescription = item.label,
                                    tint = item.tint.invoke(enabler)
                                )


                        }
                    }
                }
                Spacer(modifier = Modifier.height(OPENED_IMAGE_BUTTON_MARGIN))
            }
        }
    }
}

@Preview(name = "Dark OpenedImage", uiMode = UI_MODE_NIGHT_YES, showBackground = true)
@Preview(name = "OpenedImage", showBackground = true)
@Composable
fun OpenedImagePreview() {
    val catImage by remember { mutableStateOf(Default.PREVIEW_CAT_IMAGE) }

    val listener: (String) -> Unit = {
        when (it) {
            LIKE -> {
                catImage.liked = if (catImage.liked == 0L) 1
                else 0
            }
            ALARM -> {
                catImage.alarmTime = if (catImage.alarmTime == 0L) 1
                else 0
            }
            FAVORITE -> {
                catImage.favorite = if (catImage.favorite == 0L) 1
                else 0
            }
        }
    }

    ImageViewerTheme {
        Surface {
            OpenedImage(buttonListener = listener, image = catImage)
        }
    }
}


