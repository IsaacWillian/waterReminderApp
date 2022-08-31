package com.example.waterreminder

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavArgs
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.waterreminder.databinding.FragmentPermissionsBinding
import com.example.waterreminder.ui.MainActivity
import com.example.waterreminder.ui.fragments.TodayFragmentDirections
import com.example.waterreminder.utils.PermissionsUtils
import org.koin.android.ext.android.inject


class PermissionsFragment : Fragment() {

    private val safeArgs:PermissionsFragmentArgs by navArgs()
    private val mPermissionsUtils:PermissionsUtils by inject()
    private var _binding : FragmentPermissionsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentPermissionsBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(safeArgs.isOnboarding){
            binding.btnNextPage.visibility = View.VISIBLE
            binding.btnBack.visibility = View.INVISIBLE
            binding.btnNextPage.setOnClickListener {
                val action = TodayFragmentDirections.toWelcomeFragment(true)
                findNavController().navigate(action)
            }
        }else {
            binding.btnNextPage.visibility = View.INVISIBLE
            binding.btnBack.visibility = View.VISIBLE
            binding.btnBack.setOnClickListener {
                findNavController().popBackStack()
            }
        }

        checkAndSetSwitch()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            binding.exactAlarmSwitch.setOnClickListener {
                    mPermissionsUtils.showAlertDialogPermission()
                }
        } else {
            binding.exactAlarmSwitch.visibility = View.GONE
            binding.exactAlarmDescription.visibility = View.GONE
            binding.exactAlarmText.visibility = View.GONE
        }

        binding.batterySwitch.setOnClickListener {
                mPermissionsUtils.showBatteryPermissionDialog()
        }
    }

    override fun onResume() {
        super.onResume()
        checkAndSetSwitch()
    }

    private fun checkAndSetSwitch(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            binding.exactAlarmSwitch.isChecked = mPermissionsUtils.checkExactAlarmPermission()
        }
        binding.batterySwitch.isChecked = mPermissionsUtils.checkBatteryPermission()
    }
}