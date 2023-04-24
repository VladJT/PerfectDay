package jt.projects.perfectday.presentation.reminder

import org.koin.androidx.viewmodel.ext.android.activityViewModel

class TomorrowFragment : BaseChildFragment() {

    override val viewModel: TomorrowViewModel by activityViewModel()

    override fun setSwipeToRefreshMove() {
        binding.swipeToRefresh.setOnRefreshListener {
            viewModel.onSwipeToRefreshMove()
            binding.swipeToRefresh.isRefreshing = false
        }
    }
}