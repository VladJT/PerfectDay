package jt.projects.perfectday.presentation.reminder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import jt.projects.perfectday.R
import jt.projects.perfectday.core.GlobalViewModel
import jt.projects.perfectday.databinding.FragmentReminderBinding
import jt.projects.utils.shared_preferences.SimpleSettingsPreferences
import org.koin.android.ext.android.getKoin
import org.koin.android.ext.android.inject

class ReminderFragment : Fragment() {

    private var _binding: FragmentReminderBinding? = null
    private val binding get() = _binding!!

    private val settingsPreferences by inject<SimpleSettingsPreferences>()
    private val viewModel = getKoin().get<GlobalViewModel>()

    companion object {
        fun newInstance() = ReminderFragment()

        const val TOMORROW = 0
        const val PERIOD = 1

        var currentFragment = TOMORROW
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReminderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initViewPager()
        setTabs()
        setSwipeToRefreshMove()
        binding.reminderViewPager.setCurrentItem(currentFragment, false)
    }

    private fun setSwipeToRefreshMove() {
        binding.swipeToRefresh.setOnRefreshListener {
            viewModel.onSwipeToRefreshMove()
            binding.swipeToRefresh.isRefreshing = false
        }
    }

    private fun initViewPager() {
        binding.reminderViewPager.adapter = RemiderViewPagerAdapter(this@ReminderFragment)
    }

    private fun setTabs() {
        TabLayoutMediator(binding.reminderTabLayout, binding.reminderViewPager) { tab, position ->
            tab.text = when (position) {
                TOMORROW -> getString(R.string.today_and_tomorrow)
                PERIOD -> "${settingsPreferences.getDaysPeriodForReminderFragment()} ".plus(
                    getString(R.string.Days)
                )

                else -> getString(R.string.tomorrow)
            }
        }.attach()
    }

    override fun onDestroy() {
        currentFragment = binding.reminderTabLayout.selectedTabPosition
        _binding = null
        super.onDestroy()
    }
}