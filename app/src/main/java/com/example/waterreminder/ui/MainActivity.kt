package com.example.waterreminder.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.waterreminder.R
import com.example.waterreminder.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding:ActivityMainBinding
    val test = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        val navController = navHostFragment.navController
        navController.addOnDestinationChangedListener{_,destination,_ ->
            when(destination.id){
                R.id.permissionsFragment -> binding.bottomNavigationView.visibility = View.GONE
                R.id.firstPageOnboarding -> binding.bottomNavigationView.visibility = View.GONE
                R.id.welcomeFragment -> binding.bottomNavigationView.visibility = View.GONE
                else -> binding.bottomNavigationView.visibility = View.VISIBLE
            }
        }
        binding.bottomNavigationView.setupWithNavController(navController)

    }
}