package com.example.travelday_2

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.travelday_2.databinding.DailyListRowBinding

class DailyScheduleAdapter(val items:ArrayList<DailyItem>) : RecyclerView.Adapter<DailyScheduleAdapter.ViewHolder>() {

    interface OnItemClickListener{
        fun OnItemClick(data:DailyItem)
    }
    var itemClickListener:OnItemClickListener?=null
    inner class ViewHolder(val binding: DailyListRowBinding) : RecyclerView.ViewHolder(binding.root){
        init{
            binding.scheduleTextView.setOnClickListener{
                itemClickListener?.OnItemClick(items[adapterPosition])

            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = DailyListRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(view)

    }

    override fun onBindViewHolder(holder: DailyScheduleAdapter.ViewHolder, position: Int) {
        val item = items[position]
        holder.binding.timeTextView.text = item.hour + ":00"
        holder.binding.scheduleTextView.text = item.activity




    }

    override fun getItemCount(): Int {
        return items.size
    }


}