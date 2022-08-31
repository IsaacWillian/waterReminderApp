package com.example.waterreminder.ui.fragments

import android.animation.Animator
import android.animation.ObjectAnimator
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.AnimationSet
import android.view.animation.DecelerateInterpolator
import android.widget.ProgressBar
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.waterreminder.R
import com.example.waterreminder.adapter.DrinkListAdapter
import com.example.waterreminder.ui.ReminderViewModel
import com.example.waterreminder.databinding.FragmentTodayBinding
import com.example.waterreminder.models.Drink
import com.example.waterreminder.ui.MainActivity
import kotlinx.coroutines.runBlocking
import org.koin.androidx.viewmodel.ext.android.sharedViewModel


class TodayFragment : Fragment() {

    private val mReminderViewModel by sharedViewModel<ReminderViewModel>()
    private var _binding:FragmentTodayBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTodayBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        runBlocking {
            if(mReminderViewModel.isFirstAccess()){
                findNavController().navigate(R.id.action_todayFragment_to_firstPageOnboarding)
            }
        }

        val adapter = DrinkListAdapter(requireContext()){drink -> drinkClicked(drink)}

        binding.recyclerDrinks.adapter = adapter
        binding.recyclerDrinks.layoutManager = GridLayoutManager(requireContext(),3)
        adapter.submitList(listOf(Drink(1,75), Drink(2,100),
            Drink(3,150),Drink
        (4,250),Drink(5,300),Drink(6,350),Drink(7,400),Drink(8,500)))

        mReminderViewModel.waterOfDay.observe(viewLifecycleOwner){
            binding.progressBar.max = it.second
            binding.progressBar.changeProgressAnimation(it.first)
            binding.waterOfDay.text = it.first.toString()
            binding.goalOfDay.text = it.second.toString()
            binding.remindersMarkedoftheDay.text = it.third.toString()
        }

        mReminderViewModel.userName.observe(viewLifecycleOwner){
            if(it.isNotBlank()){
                binding.greeting.text = requireContext().getString(R.string.greeting, it)
            } else {
                binding.greeting.text = requireContext().getString(R.string.greeting_without_name)
            }
        }

        binding.btnRemoveDrink.setOnClickListener {
            if (binding.mlsToRemove.text.isNotEmpty()){
            drinkRemoveCLicked(binding.mlsToRemove.text.toString().toInt())
        }
        }

        binding.goalOfDay.setOnClickListener {
            val action = TodayFragmentDirections.toWelcomeFragment(false)
            findNavController().navigate(action)
        }

        binding.greeting.setOnClickListener {
            val action = TodayFragmentDirections.toWelcomeFragment(false)
            findNavController().navigate(action)
        }

    }



    fun drinkClicked(drink:Drink) = mReminderViewModel.drink(drink)

    fun drinkRemoveCLicked(mls:Int) = mReminderViewModel.removeDrink(mls.toInt())


    fun ProgressBar.changeProgressAnimation(percent: Int){
        ObjectAnimator.ofInt(this, "progress", percent).apply {
            duration = 800
            start()
        }
    }
}