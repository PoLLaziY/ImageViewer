package com.example.imageviewer.view.composeview.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.imageviewer.R
import com.example.imageviewer.domain.CatImage
import com.example.imageviewer.view.composeview.values.Buttons.OPENED_IMAGE_BUTTONS
import com.example.imageviewer.view.composeview.values.*
import com.example.imageviewer.view.ui.theme.ImageViewerTheme
import com.example.imageviewer.view.ui.theme.Orange

@Composable
fun OpenedImage(
    modifier: Modifier = Modifier,
    image: CatImage? = Default.PREVIEW_CAT_IMAGE,
    buttonListener: ((id: String) -> Unit)? = null
) {
    Column(
        modifier = modifier
            //.verticalScroll(state = rememberScrollState())
            .padding(start = OPENED_IMAGE_PADDING, end = OPENED_IMAGE_PADDING)
    ) {
        Spacer(modifier = Modifier.height(OPENED_IMAGE_PADDING))
        ImageHolder(Modifier.weight(1f), image)
        Spacer(modifier = Modifier.height(OPENED_IMAGE_PADDING))
        Controller(catImage = image, buttonListener = buttonListener)
        //Spacer(modifier = Modifier.height(OPENED_IMAGE_PADDING))
        //Details()
    }
}

@Composable
fun ImageHolder(modifier: Modifier = Modifier, catImage: CatImage?) {
    Card(
        modifier = modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(OPENED_IMAGE_CARD_RADIUS)
    ) {
        Image(
            painter = rememberAsyncImagePainter(
                catImage?.url,
                placeholder = painterResource(id = R.drawable.ic_launcher_background)
            ),
            contentDescription = catImage?.url
        )
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
        shape = RoundedCornerShape(
            topStart = OPENED_IMAGE_CARD_RADIUS,
            topEnd = OPENED_IMAGE_CARD_RADIUS
        )
    ) {
        Column() {
            Spacer(modifier = Modifier.height(OPENED_IMAGE_BUTTON_MARGIN))
            buttonList.forEach { list ->
                Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly) {
                    list.forEach { item ->
                            IconButton(
                                onClick = { buttonListener?.invoke(item.label) },
                                Modifier.size(OPENED_IMAGE_BUTTON_DIMEN)
                            ) {
                                Image(
                                    modifier = Modifier.size(OPENED_IMAGE_BUTTON_DIMEN),
                                    painter = painterResource(
                                        id = item.icon(item.enabler?.invoke(catImage))
                                    ),
                                    contentDescription = item.label
                                )
                            }
                    }
                }
                Spacer(modifier = Modifier.height(OPENED_IMAGE_BUTTON_MARGIN))
            }
        }
    }
}

@Composable
fun Details() {
    Box(
        Modifier
            .height(400.dp)
            .background(color = Orange)
            .padding(bottom = 20.dp)
            .fillMaxWidth()
    ) {

    }
}

@Preview(name = "Dark OpenedImage", uiMode = UI_MODE_NIGHT_YES)
@Preview(name = "OpenedImage")
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
        OpenedImage(buttonListener = listener, image = catImage)
    }
}


