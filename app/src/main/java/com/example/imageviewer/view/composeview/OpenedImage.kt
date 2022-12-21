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
import com.example.imageviewer.R
import com.example.imageviewer.domain.CatImage
import com.example.imageviewer.view.ui.theme.ImageViewerTheme
import com.example.imageviewer.view.ui.theme.Orange

//
//<dimen name="opened_image_margin">16dp</dimen>
//<dimen name="opened_image_card_corner_radius">16dp</dimen>
//<dimen name="opened_image_elevation">4dp</dimen>
//<dimen name="opened_image_swipe_button_dimen">30dp</dimen>
//<dimen name="opened_image_button_margin">8dp</dimen>
//<dimen name="opened_image_button_upper_dimen">30dp</dimen>
//<dimen name="opened_image_upper_margin">8dp</dimen>
//<dimen name="opened_image_interact_button_dimen">60dp</dimen>

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
                else R.drawable.ic_baseline_local_fire_department_24 },
        OpenedImageButton("Share") { R.drawable.ic_baseline_share_24 }
    )
)

val PREVIEW_CAT_IMAGE = CatImage(arrayListOf(), arrayListOf(), "asd", "", 1, 1, 1, 1)

@Composable
fun OpenedImage(
    catImage: LiveData<CatImage?> = MutableLiveData(PREVIEW_CAT_IMAGE),
    buttonListener: MutableLiveData<String?> = MutableLiveData(null)
) {
    Column(
        modifier = Modifier
            .verticalScroll(state = rememberScrollState())
            .fillMaxWidth()
            .wrapContentHeight()
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
            painter = painterResource(id = R.drawable.ic_launcher_background),
            contentDescription = ""
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
            buttonList.forEachIndexed { listsIndex, list ->
                Row() {
                    list.forEachIndexed { index, item ->
                        Box(
                            modifier = Modifier.weight(1f),
                            contentAlignment = Alignment.Center
                        ) {
                            IconButton(
                                onClick = { buttonListener.postValue(item.name) },
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
    ImageViewerTheme() {
        OpenedImage()
    }
}