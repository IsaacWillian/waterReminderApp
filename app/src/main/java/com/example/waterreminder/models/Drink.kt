package com.example.waterreminder.models

import com.example.waterreminder.R

data class Drink(val id:Long,val size:Int) {

    fun getImage():Int{
        return when(size){
            in 0..99 -> R.drawable.ic_water1
            in 100..399 -> R.drawable.ic_water2
            else -> R.drawable.ic_water3

        }
    }
}