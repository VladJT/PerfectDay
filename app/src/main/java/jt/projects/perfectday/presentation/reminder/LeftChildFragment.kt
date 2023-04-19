package jt.projects.perfectday.presentation.reminder

import org.koin.androidx.viewmodel.ext.android.activityViewModel

class LeftChildFragment : BaseChildFragment() {
    companion object {
        fun newInstance() = LeftChildFragment()
    }

    override val viewModel: LeftChildViewModel by activityViewModel()

    override fun setSwipeToRefreshMove() {
        binding.swipeToRefresh.setOnRefreshListener {
            viewModel.onSwipeToRefreshMove()
            binding.swipeToRefresh.isRefreshing = false
        }
    }
}