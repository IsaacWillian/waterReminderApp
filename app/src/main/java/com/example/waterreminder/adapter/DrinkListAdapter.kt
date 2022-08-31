package com.example.waterreminder.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.waterreminder.databinding.DrinkItemBinding
import com.example.waterreminder.models.Drink

class DrinkListAdapter(val context: Context, val drinkListener: (Drink) -> Unit ):ListAdapter<Drink,DrinkListAdapter.DrinkViewHolder>(DrinkComparator()){



    class DrinkViewHolder(val itemBinding:DrinkItemBinding, val context: Context, val drinkListener:(Drink)->Unit):RecyclerView.ViewHolder(itemBinding.root){

        private val drinkAnimation = AlphaAnimation(1F,0F).apply {
            duration = 500
        }

        fun bind(drink:Drink){
            itemBinding.root.setLayout(drink.size.toString(),ContextCompat.getDrawable(context,drink.getImage())!!)
            itemBinding.root.setOnClickListener {
                drinkListener(drink)
                itemBinding.root.drinkAnimation()
            }

        }
    }


    class DrinkComparator: DiffUtil.ItemCallback<Drink>(){
        override fun areItemsTheSame(oldItem: Drink, newItem: Drink) = oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Drink, newItem: Drink) = oldItem.size == newItem.size

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DrinkViewHolder {
        val itemBinding = DrinkItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return DrinkViewHolder(itemBinding,context,drinkListener)
    }

    override fun onBindViewHolder(holder: DrinkViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    override fun getItemCount() = currentList.size
}