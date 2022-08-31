package com.example.waterreminder.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.waterreminder.databinding.HistoryDayItemBinding
import com.example.waterreminder.models.History

class DaysListAdapter(val context: Context,val onDayClicked: (history: History) -> Any ):ListAdapter<History,DaysListAdapter.HistoryViewHolder>(HistoryComparator()){

    class HistoryViewHolder(val itemBinding:HistoryDayItemBinding, val context: Context, val onDayClicked: (history: History) -> Any):RecyclerView.ViewHolder(itemBinding.root){

        fun bind(history: History){
            itemBinding.dayText.text = history.day.toString()
            itemBinding.btnDay.setOnClickListener {
                onDayClicked(history)
            }
        }

    }

    class HistoryComparator: DiffUtil.ItemCallback<History>(){
        override fun areItemsTheSame(oldItem: History, newItem: History): Boolean = oldItem.day == newItem.day

        override fun areContentsTheSame(oldItem: History, newItem: History): Boolean = oldItem.waterOfDay == newItem.waterOfDay && oldItem.goalOfDay == newItem.goalOfDay

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DaysListAdapter.HistoryViewHolder {
        val itemBinding = HistoryDayItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return HistoryViewHolder(itemBinding,context, onDayClicked)
    }

    override fun onBindViewHolder(holder: DaysListAdapter.HistoryViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

}