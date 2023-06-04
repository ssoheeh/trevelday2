package com.example.travelday_2

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.travelday_2.databinding.DailyListRowBinding

class DailyScheduleAdapter(var dailyList:ArrayList<SharedViewModel.DailySchedule>) : RecyclerView.Adapter<DailyScheduleAdapter.ViewHolder>() {

    interface OnItemClickListener{
        fun OnItemClick(data:SharedViewModel.DailySchedule)
    }
    var itemClickListener:OnItemClickListener?=null
    inner class ViewHolder(val binding: DailyListRowBinding) : RecyclerView.ViewHolder(binding.root){
        init{
            binding.scheduleTextView.setOnClickListener{
                itemClickListener?.OnItemClick(dailyList[adapterPosition])

            }

        }

    }
    fun moveItem(oldPos:Int, newPos:Int){
        val item = dailyList[oldPos]
        dailyList.removeAt(oldPos)
        dailyList.add(newPos,item)
        notifyItemMoved(oldPos, newPos)
    }
    fun removeItem(pos:Int){
        dailyList.removeAt(pos)
        notifyItemRemoved(pos)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = DailyListRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(view)

    }

    override fun onBindViewHolder(holder: DailyScheduleAdapter.ViewHolder, position: Int) {
        val item = dailyList[position]
        holder.binding.timeTextView.text = item.hour.toString() +":"+item.minute.toString()
        holder.binding.scheduleTextView.text = item.task
        if (item.task.isNotBlank()) {
            holder.binding.scheduleTextView.setBackgroundResource(R.drawable.task_background_filled)
        } else {
            // Otherwise, use the default background.
            holder.binding.scheduleTextView.setBackgroundResource(R.drawable.task_background_default)
        }


    }

    override fun getItemCount(): Int {
        return dailyList.size
    }
}

