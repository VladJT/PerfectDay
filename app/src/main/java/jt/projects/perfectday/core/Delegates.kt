package jt.projects.perfectday.core

import androidx.fragment.app.Fragment
import jt.projects.perfectday.presentation.MainActivity


fun Fragment.showProgress(progress: Int) {
    (this.requireActivity() as? MainActivity)?.showProgress(progress)
}

fun Fragment.showFab(isVisible: Boolean) {
    (this.requireActivity() as? MainActivity)?.showFab(isVisible)
}

fun Fragment.showButtonBackHome(isVisible: Boolean) {
    (this.requireActivity() as? MainActivity)?.showButtonBackHome(isVisible)
}