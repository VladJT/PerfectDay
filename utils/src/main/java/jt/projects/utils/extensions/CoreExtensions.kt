package jt.projects.utils.extensions

import android.view.View
import androidx.core.view.isVisible

fun emptyString(): String = ""

fun View.hideViewInRecycler() {
    this.isVisible = false
    val params = this.layoutParams
    params.width = 0
    params.height = 0
    this.layoutParams = params
}