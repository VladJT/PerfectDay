package jt.projects.perfectday.presentation.reminder

import org.koin.androidx.viewmodel.ext.android.activityViewModel

class PeriodFragment : BaseChildFragment() {

    override val viewModel: PeriodViewModel by activityViewModel()

    override fun setSwipeToRefreshMove() {
        binding.swipeToRefresh.setOnRefreshListener {
            viewModel.onSwipeToRefreshMove()
            binding.swipeToRefresh.isRefreshing = false
        }
    }
}