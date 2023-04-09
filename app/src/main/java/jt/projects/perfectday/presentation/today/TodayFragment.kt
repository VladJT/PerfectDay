package jt.projects.perfectday.presentation.today

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import jt.projects.model.AppState
import jt.projects.model.DataModel
import jt.projects.perfectday.core.showProgress
import jt.projects.perfectday.databinding.FragmentTodayBinding
import jt.projects.perfectday.presentation.MainAdapter
import jt.projects.utils.showSnackbar
import jt.projects.utils.showToast
import org.koin.androidx.viewmodel.ext.android.viewModel


class TodayFragment : Fragment() {
    private var _binding: FragmentTodayBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance() = TodayFragment()
    }

    private val viewModel: TodayViewModel by viewModel() // НЕ привязана к жизненному циклу Activity

    private val todayAdapter: MainAdapter by lazy { MainAdapter(::onItemClick) }

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
        initViewModel()
        initRecView()
    }

    private fun initViewModel() {
        viewModel.liveDataForViewToObserve.observe(this@TodayFragment) {
            renderData(it)
        }
        viewModel.loadData()
    }

    private fun initRecView() {
        with(binding.todayRecyclerview) {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = todayAdapter
        }
    }

    private fun renderData(appState: AppState) {
        when (appState) {
            is AppState.Success -> {
                showLoadingFrame(false)
                val data = appState.data?.let { data ->
                    todayAdapter.setData(data)
                }
            }
            is AppState.Loading -> {
                showLoadingFrame(true)
                appState.progress?.let { showProgress(it) }
            }
            is AppState.Error -> {
                showLoadingFrame(false)
                showSnackbar(appState.error.message.toString())
            }
        }
    }

    private fun showLoadingFrame(isLoading: Boolean) {
        if (isLoading) {
            binding.loadingFrameLayout.visibility = View.VISIBLE
        } else {
            binding.loadingFrameLayout.visibility = View.GONE
        }
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}