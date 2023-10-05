package jt.projects.perfectday.core.extensions

import androidx.fragment.app.Fragment
import jt.projects.perfectday.core.GlobalViewModel
import jt.projects.perfectday.presentation.MainActivity
import jt.projects.perfectday.presentation.today.TodayViewModel
import org.koin.java.KoinJavaComponent.getKoin


fun Fragment.showProgress(progress: Int, status: String? = null) {
    (this.requireActivity() as? MainActivity)?.showProgress(progress, status)
}

fun Fragment.navigateToFragment(fragment: Fragment, isAddToBackStack: Boolean = false) {
    (this.requireActivity() as? MainActivity)?.navigateToFragment(fragment, isAddToBackStack)
}

fun Fragment.editScheduledEvent(id: Int) {
    (requireActivity() as? MainActivity)?.showScheduledEvent(id)
}

fun reloadAllContent() {
    getKoin().get<GlobalViewModel>().onSwipeToRefreshMove()
    getKoin().get<TodayViewModel>().onSwipeToRefreshMove()
}