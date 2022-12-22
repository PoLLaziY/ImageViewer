package com.example.imageviewer.view.composeview

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import coil.compose.rememberAsyncImagePainter
import com.example.imageviewer.R
import com.example.imageviewer.domain.CatImage
import com.example.imageviewer.view.ui.theme.ImageViewerTheme
import com.example.imageviewer.view.ui.theme.Orange

@Composable
fun OpenedImage(
    modifier: Modifier = Modifier,
    catImage: LiveData<CatImage?> = MutableLiveData(PREVIEW_CAT_IMAGE),
    buttonListener: MutableLiveData<String?> = MutableLiveData(null)
) {
    Column(
        modifier = modifier
            .verticalScroll(state = rememberScrollState())
            .padding(start = OPENED_IMAGE_PADDING, end = OPENED_IMAGE_PADDING)
    ) {
        Spacer(modifier = Modifier.height(OPENED_IMAGE_PADDING))
        ImageHolder(catImage)
        Spacer(modifier = Modifier.height(OPENED_IMAGE_PADDING))
        Controller(catImage = catImage, buttonListener = buttonListener)
        Spacer(modifier = Modifier.height(OPENED_IMAGE_PADDING))
        Details()
        Spacer(modifier = Modifier.height(OPENED_IMAGE_PADDING))
    }
}

@Composable
fun ImageHolder(catImage: LiveData<CatImage?>) {
    val image by catImage.observeAsState()
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(600.dp), shape = RoundedCornerShape(OPENED_IMAGE_CARD_RADIUS)
    ) {
        Image(
            painter = rememberAsyncImagePainter(
                image?.url,
                painterResource(id = R.drawable.ic_launcher_background)
            ),
            contentDescription = image?.url
        )
    }
}

@Composable
fun Controller(
    buttonList: List<List<OpenedImageButton>> = OPENED_IMAGE_BUTTONS,
    catImage: LiveData<CatImage?>, buttonListener: MutableLiveData<String?>
) {
    val image by catImage.observeAsState()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(), shape = RoundedCornerShape(OPENED_IMAGE_CARD_RADIUS)
    ) {
        Column() {
            Spacer(modifier = Modifier.height(OPENED_IMAGE_BUTTON_MARGIN))
            buttonList.forEach { list ->
                Row() {
                    list.forEach { item ->
                        Box(
                            modifier = Modifier.weight(1f),
                            contentAlignment = Alignment.Center
                        ) {
                            IconButton(
                                onClick = { buttonListener.value = (item.name) },
                                Modifier.size(OPENED_IMAGE_BUTTON_DIMEN)
                            ) {
                                Image(
                                    modifier = Modifier.size(OPENED_IMAGE_BUTTON_DIMEN),
                                    painter = painterResource(id = item.icon.invoke(image)),
                                    contentDescription = item.name
                                )
                            }
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

@Preview(name = "Dark", uiMode = UI_MODE_NIGHT_YES)
@Preview(name = "Light")
@Composable
fun OpenedImagePreview() {
    val catImage = MutableLiveData(PREVIEW_CAT_IMAGE)
    val listener = MutableLiveData<String?>(null)

    listener.observeForever {
        when (it) {
            "Like" -> {
                catImage.value?.liked = if (catImage.value?.liked == 0L) 1
                else 0
            }
            "Alarm" -> {
                catImage.value?.alarmTime = if (catImage.value?.alarmTime == 0L) 1
                else 0
            }
            "Favorite" -> {
                catImage.value?.favorite = if (catImage.value?.favorite == 0L) 1
                else 0
            }
        }
    }

    ImageViewerTheme {
        OpenedImage(catImage = catImage, buttonListener = listener)
    }
}

val OPENED_IMAGE_PADDING = 16.dp
val OPENED_IMAGE_CARD_RADIUS = 16.dp
val OPENED_IMAGE_BUTTON_DIMEN = 60.dp
val OPENED_IMAGE_BUTTON_MARGIN = 8.dp

data class OpenedImageButton(val name: String, val icon: (CatImage?) -> Int)

val OPENED_IMAGE_BUTTONS: List<List<OpenedImageButton>> = listOf(
    listOf(
        OpenedImageButton("Left") { R.drawable.ic_baseline_chevron_left_24 },
        OpenedImageButton("Like") {
            if (it?.liked ?: 0 > 0) R.drawable.ic_baseline_favorite_24
            else R.drawable.ic_baseline_favorite_border_24
        },
        OpenedImageButton("Right") { R.drawable.ic_baseline_chevron_right_24 }
    ),
    listOf(
        OpenedImageButton("Save") { R.drawable.ic_baseline_download_24 },
        OpenedImageButton("Alarm") {
            if (it?.alarmTime ?: 0 > 0) R.drawable.ic_baseline_cancel_24
            else R.drawable.ic_baseline_watch_later_24
        },
        OpenedImageButton("Favorite") {
            if (it?.favorite ?: 0 > 0) R.drawable.ic_baseline_local_fire_department_orange_24
            else R.drawable.ic_baseline_local_fire_department_24
        },
        OpenedImageButton("Share") { R.drawable.ic_baseline_share_24 }
    )
)

val PREVIEW_CAT_IMAGE = CatImage(
    "asd",
    "https://i.picsum.photos/id/866/200/300.jpg?hmac=rcadCENKh4rD6MAp6V_ma-AyWv641M4iiOpe1RyFHeI",
    1,
    1,
    1,
    1
)
