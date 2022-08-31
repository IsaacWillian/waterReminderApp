package com.example.waterreminder.ui.fragments

import android.animation.ObjectAnimator
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.widget.ProgressBar
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.waterreminder.R
import com.example.waterreminder.adapter.DaysListAdapter
import com.example.waterreminder.adapter.GraphAdapter
import com.example.waterreminder.databinding.FragmentHistoryBinding
import com.example.waterreminder.models.History
import com.example.waterreminder.ui.HistoryViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class HistoryFragment : Fragment() {

    private val mHistoryViewModel : HistoryViewModel by viewModel()
    private var _binding:FragmentHistoryBinding?= null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentHistoryBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        mHistoryViewModel.updateHistories()
        hideDayHistory()


    }

    fun hideDayHistory(){
        binding.dailyWater.visibility = View.INVISIBLE
        binding.dailyGoal.visibility = View.INVISIBLE
        binding.waterProgressBar.visibility = View.INVISIBLE
        binding.advice.visibility = View.VISIBLE
    }

    fun showDayHistory(){
        binding.dailyWater.visibility = View.VISIBLE
        binding.dailyGoal.visibility = View.VISIBLE
        binding.waterProgressBar.visibility = View.VISIBLE
        binding.advice.visibility = View.INVISIBLE
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val monthOverviewRecycler = binding.chartBars
        val monthOverviewAdapter = GraphAdapter(requireContext())
        monthOverviewRecycler.adapter = monthOverviewAdapter
        monthOverviewRecycler.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)

        val daysListRecycler = binding.daysList
        val daysListAdapter = DaysListAdapter(requireContext()){history -> onDayClicked(history)}
        daysListRecycler.adapter = daysListAdapter
        daysListRecycler.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)


        mHistoryViewModel.historysByMonth.observe(viewLifecycleOwner){
            it?.let {
                monthOverviewAdapter.submitList(it)
                daysListAdapter.submitList(it)
            }
        }

        mHistoryViewModel.currentMonthAndYear.observe(viewLifecycleOwner){
            binding.currentMonth.text = getStringByMonth(it.first)
            binding.currentYear.text = it.second.toString()
        }

        binding.btnPreviousMonth.setOnClickListener {
            mHistoryViewModel.previousMonth()
        }

        binding.btnNextMonth.setOnClickListener {
            mHistoryViewModel.nextMonth()
        }

    }

    fun onDayClicked(history:History){
        binding.dailyGoal.text = requireContext().getString(R.string.water_with_ml,history.goalOfDay)
        binding.dailyWater.text = requireContext().getString(R.string.water_with_ml,history.waterOfDay)
        showDayHistory()
        binding.waterProgressBar.smoothProgress(((history.waterOfDay.toFloat())/(history.goalOfDay.toFloat())*100).toInt())

    }

    fun ProgressBar.smoothProgress(percent: Int){
        val animation = ObjectAnimator.ofInt(this, "progress", percent)
        animation.duration = 800
        animation.interpolator = DecelerateInterpolator()
        animation.start()
    }

    private fun getStringByMonth(month:Int):String{
        return when(month){
            1 -> getString(R.string.january)
            2 -> getString(R.string.february)
            3 -> getString(R.string.march)
            4 -> getString(R.string.april)
            5 -> getString(R.string.may)
            6 -> getString(R.string.june)
            7 -> getString(R.string.july)
            8 -> getString(R.string.august)
            9 -> getString(R.string.september)
            10 -> getString(R.string.october)
            11 -> getString(R.string.november)
            12 -> getString(R.string.december)
            else -> ""
        }
    }




}