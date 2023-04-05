package jt.projects.perfectday.presentation.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import jt.projects.perfectday.databinding.FragmentSettingsBinding
import jt.projects.perfectday.presentation.MainActivity

class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedState: Bundle?): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vkLogin.setOnClickListener {
            (requireActivity() as MainActivity).launcherVk.launch(listOf())
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}