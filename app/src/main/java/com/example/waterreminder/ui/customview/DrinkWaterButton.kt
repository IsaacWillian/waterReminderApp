package com.example.waterreminder.ui.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.AnimationSet
import android.view.animation.TranslateAnimation
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.waterreminder.R
import com.example.waterreminder.adapter.DrinkListAdapter
import com.example.waterreminder.databinding.DrinkWaterButtonBinding
import com.example.waterreminder.models.Drink

class DrinkWaterButton @JvmOverloads constructor(context: Context,attrs:AttributeSet? = null, defStyleAttr:Int = 0) : ConstraintLayout(context,attrs,defStyleAttr) {
        private val waterText : TextView
        private val btnImage: ImageView
        private var alphaAnimation:AlphaAnimation
        private var translateAnimation: TranslateAnimation
        private var animationSet:AnimationSet

        private val binding: DrinkWaterButtonBinding =
                DrinkWaterButtonBinding.inflate(LayoutInflater.from(context),this,true)


        init{
                waterText = binding.text
                btnImage = binding.waterDrawable
                alphaAnimation = AlphaAnimation(1F,0F).apply {
                        duration = 300
                }
                translateAnimation = TranslateAnimation(btnImage.x,btnImage.x,btnImage.y,btnImage.y+100F).apply {
                        duration = 400
                }
                animationSet = AnimationSet(true).apply {
                        addAnimation(translateAnimation)
                        addAnimation(alphaAnimation)
                }
        }

        fun setLayout(mlOfWater:String,newImage:Drawable){
                waterText.text = mlOfWater + "ml"
                btnImage.setImageDrawable(newImage)
        }
        fun drinkAnimation(){
                btnImage.startAnimation(animationSet)

        }
}