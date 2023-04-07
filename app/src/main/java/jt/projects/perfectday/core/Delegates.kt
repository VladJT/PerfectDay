package jt.projects.perfectday.core

import androidx.fragment.app.Fragment
import jt.projects.perfectday.presentation.MainActivity


fun Fragment.showLoadingFrame(isLoading: Boolean) {
    (this.requireActivity() as? MainActivity)?.showLoadingFrame(isLoading)
}

fun Fragment.showProgress(progress: Int) {
    (this.requireActivity() as? MainActivity)?.showProgress(progress)
}