package com.mionix.baseapp.utils

import android.content.Context
import android.graphics.Bitmap

object KotlinUtils {
    fun dipToPx(context: Context, dpValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }


}