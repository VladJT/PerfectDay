package jt.projects.perfectday.presentation.congratulation_bottom_dialog

import android.os.Bundle
import android.view.*
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import jt.projects.perfectday.databinding.BottomFragmentCongratulationBinding

class CongratulationBottomDialogFragment : BottomSheetDialogFragment() {
    private var _binding: BottomFragmentCongratulationBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, cont: ViewGroup?, savedState: Bundle?): View {
        _binding = BottomFragmentCongratulationBinding.inflate(inflater, cont, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnYes.setOnClickListener {
            setFragmentResult(BUTTON_YES_CLICK, bundleOf())
            dismiss()
        }
        binding.btnNo.setOnClickListener {
            dismiss()
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    companion object {
        const val BUTTON_YES_CLICK = "button_yes_click"
    }
}