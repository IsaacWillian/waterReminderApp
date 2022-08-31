package com.example.waterreminder.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.waterreminder.PermissionsFragmentArgs
import com.example.waterreminder.R
import com.example.waterreminder.databinding.FragmentWelcomeBinding
import com.example.waterreminder.ui.MainActivity
import com.example.waterreminder.ui.ReminderViewModel
import kotlinx.coroutines.runBlocking
import org.koin.androidx.viewmodel.ext.android.sharedViewModel


class WelcomeFragment : Fragment() {

    private val safeArgs: PermissionsFragmentArgs by navArgs()
    private val mReminderViewModel:ReminderViewModel by sharedViewModel()
    private var _binding : FragmentWelcomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentWelcomeBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mReminderViewModel.userName.observe(viewLifecycleOwner){
            binding.editName.setText(it)
        }

        mReminderViewModel.goalOfDay.observe(viewLifecycleOwner){
            binding.dailyGoal.progress = it/500
        }

        if(safeArgs.isOnboarding) {
            binding.btnBegin.text = getString(R.string.begin_welcome)
        } else {
            binding.btnBegin.text = getString(R.string.update_welcome)
        }


        binding.dailyGoal.setOnSeekBarChangeListener(object:SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                binding.dailyGoalValue.text = requireContext().getString(R.string.water_with_ml, (p1*500))
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {

            }

            override fun onStopTrackingTouch(p0: SeekBar?) {

            }

        })

        binding.btnBegin.setOnClickListener {
            mReminderViewModel.saveFirstAccess(false)
            val username = binding.editName.text.toString()
            mReminderViewModel.saveUserName(username)
            val goalOfDay = binding.dailyGoal.progress
            mReminderViewModel.saveGoalOfDay(goalOfDay*500)
            findNavController().navigate(R.id.action_welcomeFragment_to_todayFragment)
        }
    }

}