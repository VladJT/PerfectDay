package jt.projects.utils.extensions

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import coil.load
import jt.projects.utils.R

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

fun ImageView.loadWithPlaceHolder(imageUrl: String) {
    this.load(imageUrl) {
        error(R.drawable.dr_place_holder)
    }
}