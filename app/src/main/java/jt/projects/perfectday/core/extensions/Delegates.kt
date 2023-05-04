package jt.projects.perfectday.core.extensions

import android.widget.TextView
import androidx.fragment.app.Fragment
import jt.projects.perfectday.core.translator.GoogleTranslator
import jt.projects.perfectday.presentation.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
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