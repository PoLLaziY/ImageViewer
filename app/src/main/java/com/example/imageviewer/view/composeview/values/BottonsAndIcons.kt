package com.example.imageviewer.view.composeview.values

import androidx.annotation.DrawableRes
import androidx.compose.ui.Alignment
import com.example.imageviewer.R
import com.example.imageviewer.domain.CatImage

data class ControlPanelButton(
    val label: String,
    val icon: ((enabled: Boolean?) -> Int),
    val enabler: ((CatImage?) -> Boolean?)? = null
)

data class BottomBarIcon(val label: String, @DrawableRes val icon: Int)

data class ImageStateIcon(
    val name: String,
    @DrawableRes val icon: Int,
    val alignment: Alignment,
    val enabler: (CatImage?) -> Boolean
)

object Buttons {

    val BOTTOM_BAR_ITEMS: List<BottomBarIcon> = listOf(
        BottomBarIcon(HOME, R.drawable.ic_baseline_home_24),
        BottomBarIcon(SEARCH, R.drawable.ic_baseline_search_24),
        BottomBarIcon(FAVORITE, R.drawable.ic_baseline_favorite_border_24)
    )

    val OPENED_IMAGE_BUTTONS: List<List<ControlPanelButton>> = listOf(
        listOf(
            ControlPanelButton(LEFT, { R.drawable.ic_baseline_chevron_left_24 }),
            ControlPanelButton(LIKE, {
                if (it == true) R.drawable.ic_baseline_favorite_24
                else R.drawable.ic_baseline_favorite_border_24
            }, { (it?.liked ?: return@ControlPanelButton null) > 0 }),
            ControlPanelButton(RIGHT, { R.drawable.ic_baseline_chevron_right_24 })
        ),
        listOf(
            ControlPanelButton(SAVE, { R.drawable.ic_baseline_download_24 }),
            ControlPanelButton(ALARM, {
                if (it == true) R.drawable.ic_baseline_cancel_24
                else R.drawable.ic_baseline_watch_later_24
            }, { (it?.alarmTime ?: return@ControlPanelButton null) > 0 }),
            ControlPanelButton(FAVORITE, {
                if (it == true) R.drawable.ic_baseline_local_fire_department_orange_24
                else R.drawable.ic_baseline_local_fire_department_24
            }, { (it?.favorite ?: return@ControlPanelButton null) > 0 }),
            ControlPanelButton(SHARE, { R.drawable.ic_baseline_share_24 })
        )
    )

    val IMAGE_STATE_ICONS = listOf(
        ImageStateIcon(
            LIKE,
            R.drawable.ic_baseline_favorite_24,
            Alignment.TopStart
        ) { it?.liked ?: 0 > 0 },
        ImageStateIcon(
            FAVORITE,
            R.drawable.ic_baseline_star_rate_24,
            Alignment.TopEnd
        ) { it?.favorite ?: 0 > 0 },
        ImageStateIcon(
            WATCHED,
            R.drawable.ic_baseline_remove_red_eye_24,
            Alignment.BottomStart
        ) { it?.watched ?: 0 > 0 },
        ImageStateIcon(
            ALARM,
            R.drawable.ic_baseline_watch_later_24,
            Alignment.BottomEnd
        ) { it?.alarmTime ?: 0 > 0 }
    )

}