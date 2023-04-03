package jt.projects.utils

import android.app.Activity
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import jt.projects.utils.R


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