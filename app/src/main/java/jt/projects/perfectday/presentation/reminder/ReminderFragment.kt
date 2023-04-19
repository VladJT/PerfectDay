package jt.projects.perfectday.presentation.reminder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import jt.projects.perfectday.databinding.FragmentReminderBinding
import jt.projects.utils.shared_preferences.SimpleSettingsPreferences
import org.koin.android.ext.android.inject

class ReminderFragment : Fragment() {

    private var _binding: FragmentReminderBinding? = null
    private val binding get() = _binding!!

    private val settingsPreferences by inject<SimpleSettingsPreferences>()

    companion object {
        fun newInstance() = ReminderFragment()
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
        navigateToChildFragment(LeftChildFragment.newInstance())
        initToggleButtons()
    }

    private fun initToggleButtons() {
        binding.buttonTomorrow.setOnClickListener {
            navigateToChildFragment(LeftChildFragment.newInstance())
        }

        binding.buttonAllTime.setOnClickListener {
            navigateToChildFragment(RightChildFragment.newInstance())
        }

        binding.buttonAllTime.text =
            "${settingsPreferences.getDaysPeriodForReminderFragment()} Дней"
    }

    private fun navigateToChildFragment(fragment: Fragment) {
        childFragmentManager.beginTransaction()
            .replace(binding.reminderFragmentContainer.id, fragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .commit()

    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}