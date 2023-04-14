package jt.projects.utils.extensions

import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView

fun emptyString(): String = ""

fun View.hideViewInRecycler() {
    this.isVisible = false
    this.layoutParams = RecyclerView.LayoutParams(0, 0)
}

fun View.showViewInRecycler() {
    val width = ViewGroup.LayoutParams.MATCH_PARENT
    val height = ViewGroup.LayoutParams.WRAP_CONTENT
    this.isVisible = true
    this.layoutParams = RecyclerView.LayoutParams(width, height)
}