package jt.projects.perfectday.presentation.reminder

import jt.projects.perfectday.core.BaseViewModel
import jt.projects.utils.VM_REMINDER_PERIOD
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import org.koin.core.qualifier.named

class PeriodFragment : BaseChildFragment() {

    override val viewModel: BaseViewModel by activityViewModel(named(VM_REMINDER_PERIOD))

    override fun setSwipeToRefreshMove() {
        binding.swipeToRefresh.setOnRefreshListener {
            viewModel.onSwipeToRefreshMove()
            binding.swipeToRefresh.isRefreshing = false
        }
    }
}