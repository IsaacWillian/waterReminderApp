package com.example.waterreminder

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.waterreminder.databinding.FragmentFirstPageOnboardingBinding
import com.example.waterreminder.ui.MainActivity


class FirstPageOnboarding : Fragment() {


    private var _binding : FragmentFirstPageOnboardingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFirstPageOnboardingBinding.inflate(inflater,container,false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnNextPage.setOnClickListener {
            val action = FirstPageOnboardingDirections.toPermissionFragment(true)
            findNavController().navigate(action)
        }

    }


}