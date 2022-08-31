package com.example.waterreminder.ui.fragments

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.waterreminder.FirstPageOnboardingDirections
import com.example.waterreminder.R
import com.example.waterreminder.adapter.ReminderListAdapter
import com.example.waterreminder.databinding.FragmentReminderListBinding
import com.example.waterreminder.models.ReminderForRecycle
import com.example.waterreminder.ui.ReminderViewModel
import com.example.waterreminder.utils.PermissionsUtils
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class ReminderList : Fragment() {

    private val mReminderViewModel by sharedViewModel<ReminderViewModel>()
    private var _binding: FragmentReminderListBinding? = null
    private val mPermissionsUtils : PermissionsUtils by inject()
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentReminderListBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recycler = binding.remindersRecycler
        val adapter = ReminderListAdapter(requireContext(),
            ::deleteReminderListener,
            ::reminderClicked)
        recycler.adapter = adapter
        recycler.layoutManager = LinearLayoutManager(requireContext())

        binding.btnNewReminder.setOnClickListener {
            findNavController().navigate(R.id.action_reminderList_to_newReminderFragment)
        }

        binding.btnHelp.setOnClickListener {
            val action = ReminderListDirections.toPermissionFragment(false)
           findNavController().navigate(action)
        }

        mReminderViewModel.remindersForRecycler.observe(viewLifecycleOwner) {
            it.let { adapter.submitList(it.toList()) }
        }


    }

    fun deleteReminderListener(reminderForRecycle: ReminderForRecycle) = mReminderViewModel.deleteReminder(reminderForRecycle.reminder)


    fun reminderClicked(reminderForRecycle: ReminderForRecycle) = mReminderViewModel.reminderClicked(reminderForRecycle)



    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}