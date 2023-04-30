package jt.projects.perfectday.presentation.reminder

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import jt.projects.perfectday.core.BaseAdapter
import jt.projects.utils.shared_preferences.SimpleSettingsPreferences
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import java.time.LocalDate

class PeriodFragment : BaseChildFragment() {

    private val sharedPref by inject<SimpleSettingsPreferences>()

    override fun initRecView(reminderAdapter: BaseAdapter) {
        with(binding.reminderRecyclerview) {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = reminderAdapter
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel
                    .getResultRecyclerByPeriod(
                        LocalDate.now(), LocalDate.now()
                            .plusDays(sharedPref.getDaysPeriodForReminderFragment())
                    )
                    .collect {
                        reminderAdapter.setData(it)
                    }
            }
        }
    }
}