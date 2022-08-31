package com.example.waterreminder.adapter

import android.content.Context
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.waterreminder.databinding.HistoryChartItemBinding
import com.example.waterreminder.models.History

class GraphAdapter(val context: Context):ListAdapter<History,GraphAdapter.HistoryViewHolder>(HistoryComparator()) {


    class HistoryViewHolder(val itemBinding:HistoryChartItemBinding, val context: Context): RecyclerView.ViewHolder(itemBinding.root){

        fun bind(history: History){
            itemBinding.dayNumber.text = history.day.toString()

            itemBinding.bar.alpha = ((history.waterOfDay.toFloat()/history.goalOfDay.toFloat())) + 0.2f
            val newParams = itemBinding.bar.layoutParams

            val px = 130 * context.resources.displayMetrics.density
            newParams.height = ( px * history.waterOfDay.toFloat()/history.goalOfDay.toFloat()).toInt() + 1 //If height = 0 he stay full size of the view
            itemBinding.bar.layoutParams = newParams

        }


    }

    class HistoryComparator:DiffUtil.ItemCallback<History>(){
        override fun areItemsTheSame(oldItem: History, newItem: History): Boolean = oldItem.day == newItem.day

        override fun areContentsTheSame(oldItem: History, newItem: History): Boolean = oldItem.waterOfDay == newItem.waterOfDay && oldItem.goalOfDay == newItem.goalOfDay

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val itemBinding = HistoryChartItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return HistoryViewHolder(itemBinding,context)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    override fun getItemCount() = currentList.size
}