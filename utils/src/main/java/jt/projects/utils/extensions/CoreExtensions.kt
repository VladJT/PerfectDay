package jt.projects.utils.extensions

import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView

fun emptyString(): String = ""

fun View.hideViewInRecycler() {
    this.isVisible = false
    this.layoutParams = RecyclerView.LayoutParams(0, 0)
}