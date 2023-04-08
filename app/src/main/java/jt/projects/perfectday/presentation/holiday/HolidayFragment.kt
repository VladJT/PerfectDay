package jt.projects.perfectday.presentation.holiday

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import jt.projects.perfectday.databinding.FragmentHolidayBinding

class HolidayFragment:Fragment() {

    private var _binding:FragmentHolidayBinding? = null
    private val binding:FragmentHolidayBinding get() = _binding!!

    companion object{
        fun newInstance()=HolidayFragment()
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHolidayBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}