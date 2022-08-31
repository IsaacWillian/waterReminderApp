package com.example.waterreminder.ui.customview

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.example.waterreminder.R

class TestView(context:Context,attrs:AttributeSet?):AppCompatTextView(context,attrs) {

    fun setStatus(status:String){
        val color = when(status){
            "a" -> R.color.bluePrimary
            "b" -> R.color.start_accent
            "c" -> R.color.black
            else -> R.color.bluePrimary40
        }
        textSize = 17f
        text = status
        setTextColor(color)

    }
}