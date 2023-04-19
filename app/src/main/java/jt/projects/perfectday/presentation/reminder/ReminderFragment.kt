package jt.projects.perfectday.presentation.reminder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction.TRANSIT_FRAGMENT_FADE
import jt.projects.perfectday.databinding.FragmentReminderBinding
import jt.projects.utils.shared_preferences.SimpleSettingsPreferences
import org.koin.android.ext.android.inject

class ReminderFragment : Fragment() {

    private var _binding: FragmentReminderBinding? = null
    private val binding get() = _binding!!

    private val settingsPreferences by inject<SimpleSettingsPreferences>()

    companion object {
        fun newInstance() = ReminderFragment()

        const val LEFT_CHILD = "left"
        const val RIGHT_CHILD = "right"

        var currentFragment = LEFT_CHILD
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
        initCurrentChild()
        initToggleButtons()
    }

    private fun initCurrentChild() {
        if (currentFragment == LEFT_CHILD) {
            binding.toggleButtons.check(binding.buttonLeftChildFragment.id)
            navigateToChildFragment(LeftChildFragment.newInstance())
        } else {
            binding.toggleButtons.check(binding.buttonRightChildFragment.id)
            navigateToChildFragment(RightChildFragment.newInstance())
        }
    }

    private fun initToggleButtons() {
        // LEFT SIDE
        binding.buttonLeftChildFragment.setOnClickListener {
            if (currentFragment != LEFT_CHILD) {
                navigateToChildFragment(LeftChildFragment.newInstance())
                currentFragment = LEFT_CHILD
            }
        }

        // RIGHT SIDE
        binding.buttonRightChildFragment.setOnClickListener {
            if (currentFragment != RIGHT_CHILD) {
                navigateToChildFragment(RightChildFragment.newInstance())
                currentFragment = RIGHT_CHILD
            }
        }
        binding.buttonRightChildFragment.text =
            "${settingsPreferences.getDaysPeriodForReminderFragment()} Дней"
    }

    private fun navigateToChildFragment(fragment: Fragment) {
        childFragmentManager.beginTransaction()
            .replace(binding.reminderFragmentContainer.id, fragment)
            .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
            .setTransition(TRANSIT_FRAGMENT_FADE)
            .commit()

    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}