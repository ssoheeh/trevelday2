package com.example.travelday_2

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.travelday_2.databinding.DateListRowBinding
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class DateListAdapter(val context: Context, val items: ArrayList<SharedViewModel.Date>) :
    RecyclerView.Adapter<DateListAdapter.ViewHolder>() {
    interface OnItemClickListener {
        fun onItemClick(data: SharedViewModel.Date)
    }
    var itemClickListener: OnItemClickListener? = null

    inner class ViewHolder(val binding: DateListRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: SharedViewModel.Date) {
            val adapter =DailyScheduleAdapter(item.dailyScheduleList)
            binding.innerRecyclerview.adapter = adapter
            binding.innerRecyclerview.layoutManager = LinearLayoutManager(context)
        }
        init {
            binding.addButtonNew.setOnClickListener {
                itemClickListener?.onItemClick(items[adapterPosition])
            }
        }
    }
    fun moveItem(oldPos:Int, newPos:Int){
        val item = items[oldPos]
        items.removeAt(oldPos)
        items.add(newPos,item)
        notifyItemMoved(oldPos, newPos)
    }
    fun removeItem(pos:Int){
        items.removeAt(pos)
        notifyItemRemoved(pos)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DateListRowBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }
    override fun getItemCount(): Int {
        return items.size    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
        holder.binding.dateTextView.text = "• " + item.date
    }
}

