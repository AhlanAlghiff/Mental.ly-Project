package com.c242ps188.mentally_app.util

import android.app.UiModeManager
import android.content.Context

fun Context.isDarkModeEnabled(): Boolean {
    val uiModeManager = getSystemService(Context.UI_MODE_SERVICE) as UiModeManager
    return uiModeManager.nightMode == UiModeManager.MODE_NIGHT_YES
}