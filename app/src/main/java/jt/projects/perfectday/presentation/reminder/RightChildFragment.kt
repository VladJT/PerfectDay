package jt.projects.perfectday.presentation.reminder

import org.koin.androidx.viewmodel.ext.android.activityViewModel

class RightChildFragment : BaseChildFragment() {
    companion object {
        fun newInstance() = RightChildFragment()
    }

    override val viewModel: RightChildViewModel by activityViewModel()
}