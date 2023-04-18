package jt.projects.utils.extensions

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
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

/**
 * ACTIVITY EXTENSIONS
 */
fun Activity.showSnackbar(text: String) {
    Snackbar.make(
        this.findViewById(android.R.id.content),
        text,
        Snackbar.LENGTH_SHORT
    ).show()
}

fun Activity.showToast(text: String) {
    Toast.makeText(
        this,
        text,
        Toast.LENGTH_SHORT
    ).show()
}

/**
 * FRAGMENT EXTENSIONS
 */
fun Fragment.showSnackbar(text: String) {
    Snackbar.make(
        requireActivity().findViewById(android.R.id.content),
        text,
        Snackbar.LENGTH_SHORT
    ).show()
}

fun ImageView.loadWithPlaceHolder(imageUrl: String) {
    this.load(imageUrl) {
        error(R.drawable.dr_place_holder)
    }
}