package jt.projects.perfectday.presentation.today

import android.os.Bundle
import android.view.*
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import jt.projects.model.DataModel
import jt.projects.perfectday.databinding.FragmentTodayBinding
import jt.projects.perfectday.presentation.today.adapter.main.MainListAdapter
import jt.projects.utils.showToast
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class TodayFragment : Fragment() {
    private var _binding: FragmentTodayBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TodayViewModel by viewModel() // НЕ привязана к жизненному циклу Activity
    private val todayAdapter by lazy { MainListAdapter() }

    private fun onItemClick(data: DataModel) {
        if (data is DataModel.ScheduledEvent){
            viewModel.deleteScheduledEvent(data.id)
        }else {
            requireActivity().showToast(data.toString())
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTodayBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initRecView()
    }

    private fun initRecView() {
        binding.todayRecyclerview.adapter = todayAdapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.resultRecycler.collect(todayAdapter::submitList)
            }
        }
    }

    private fun showLoadingFrame(isLoading: Boolean) {
        binding.loadingFrameLayout.isVisible = isLoading
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}