package jt.projects.perfectday.presentation.reminder

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import jt.projects.perfectday.core.BaseAdapter
import kotlinx.coroutines.launch
import java.time.LocalDate

class TomorrowFragment : BaseChildFragment() {

    override fun initRecView(reminderAdapter: BaseAdapter) {
        with(binding.reminderRecyclerview) {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = reminderAdapter
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel
                    .getResultRecyclerByPeriod(LocalDate.now(), LocalDate.now().plusDays(1))
                    .collect {
                        reminderAdapter.setData(it)
                    }
            }
        }
    }
}