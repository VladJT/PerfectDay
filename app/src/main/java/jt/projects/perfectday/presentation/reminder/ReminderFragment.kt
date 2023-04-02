package jt.projects.perfectday.presentation.reminder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import jt.projects.perfectday.databinding.FragmentCalendarBinding
import jt.projects.perfectday.databinding.FragmentReminderBinding
import jt.projects.perfectday.databinding.FragmentTodayBinding

class ReminderFragment : Fragment(){
    private var _binding: FragmentReminderBinding? = null
    private val binding get() = _binding!!

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


    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}