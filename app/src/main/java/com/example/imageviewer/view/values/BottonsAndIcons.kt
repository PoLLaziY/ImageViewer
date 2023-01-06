package com.example.imageviewer.view.values

import androidx.annotation.DrawableRes
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import com.example.imageviewer.R
import com.example.imageviewer.domain.CatImage

data class ControlPanelButton(
    val label: String,
    val icon: ((enabled: Boolean?) -> Int),
    val enabler: ((CatImage?) -> Boolean?)? = null,
    val tint: @Composable (Boolean?) -> Color = { MaterialTheme.colors.onPrimary }
)

data class BottomBarIcon(
    val label: String,
    val icon: @Composable (Boolean) -> Int,
    val enabler: @Composable (String?) -> Boolean = { it === label }
) {
    constructor(
        label: String,
        icon: Int,
        enabler: @Composable (String?) -> Boolean = { it === label }
    ) : this(
        label, { icon }, enabler
    )
}

data class ImageStateIcon(
    val name: String,
    @DrawableRes val icon: Int,
    val alignment: Alignment,
    val tint: @Composable () -> Color = { MaterialTheme.colors.onPrimary },
    val enabler: (CatImage?) -> Boolean
)

object Buttons {

    val BOTTOM_BAR_ITEMS: List<BottomBarIcon> = listOf(
        BottomBarIcon(HOME, R.drawable.ic_baseline_home_24),
        BottomBarIcon(SEARCH, R.drawable.ic_baseline_search_24),
        BottomBarIcon(FAVORITE, R.drawable.ic_baseline_favorite_border_24),
        BottomBarIcon(
            THEME,
            enabler = { MaterialTheme.colors.isLight },
            icon = { if (it) R.drawable.ic_baseline_wb_sunny_24 else R.drawable.ic_baseline_nights_stay_24 })
    )

    val OPENED_IMAGE_BUTTONS: List<List<ControlPanelButton>> = listOf(
        listOf(
            ControlPanelButton(
                label = LEFT,
                icon = { R.drawable.ic_baseline_chevron_left_24 },
                tint = { MaterialTheme.colors.secondary }
            ),
            ControlPanelButton(
                label = LIKE,
                icon = {
                    if (it == true) R.drawable.ic_baseline_favorite_24
                    else R.drawable.ic_baseline_favorite_border_24
                },
                enabler = { (it?.liked ?: return@ControlPanelButton null) > 0 },
                tint = { if (it == true) Color.Red else MaterialTheme.colors.onPrimary }
            ),
            ControlPanelButton(
                label = RIGHT,
                icon = { R.drawable.ic_baseline_chevron_right_24 },
                tint = { MaterialTheme.colors.secondary }
            )
        ),
        listOf(
            ControlPanelButton(SAVE, { R.drawable.ic_baseline_download_24 }),
            ControlPanelButton(
                label = ALARM,
                icon = {
                    if (it == true) R.drawable.ic_baseline_cancel_24
                    else R.drawable.ic_baseline_watch_later_24
                },
                enabler = { (it?.alarmTime ?: return@ControlPanelButton null) > 0 },
                tint = { if (it == true) MaterialTheme.colors.secondary else MaterialTheme.colors.onPrimary }
            ),
            ControlPanelButton(
                label = FAVORITE,
                icon = {
                    if (it == true) R.drawable.ic_baseline_local_fire_department_orange_24
                    else R.drawable.ic_baseline_local_fire_department_24
                },
                enabler = { (it?.favorite ?: return@ControlPanelButton null) > 0 },
                tint = { if (it == true) MaterialTheme.colors.secondary else MaterialTheme.colors.onPrimary }
            ),
            ControlPanelButton(SHARE, { R.drawable.ic_baseline_share_24 })
        )
    )

    val IMAGE_STATE_ICONS = listOf(
        ImageStateIcon(
            LIKE,
            R.drawable.ic_baseline_favorite_24,
            Alignment.TopStart,
            tint = { Color.Red }
        ) { it?.liked ?: 0 > 0 },
        ImageStateIcon(
            FAVORITE,
            R.drawable.ic_baseline_star_rate_24,
            Alignment.TopEnd,
            tint = { Color.Yellow }
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