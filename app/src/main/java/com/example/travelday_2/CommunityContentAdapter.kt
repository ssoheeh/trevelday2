package com.example.travelday_2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.travelday_2.databinding.CommunityListRowBinding
import com.example.travelday_2.databinding.DailyListRowBinding

class CommunityContentAdapter(val items: MutableList<CommunityContent>) :
    RecyclerView.Adapter<CommunityContentAdapter.ViewHolder>() {

    interface OnItemClickListener{
        fun OnItemClick(data:CommunityContent)
    }
    var itemClickListener: CommunityContentAdapter.OnItemClickListener?=null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommunityContentAdapter.ViewHolder {
        val view = CommunityListRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    // 생성된 View Holder에 데이터를 바인딩 해주는 메서드
    override fun onBindViewHolder(holder: CommunityContentAdapter.ViewHolder, position: Int) {
        holder.bindItems(items[position])
    }
    override fun getItemCount(): Int {
        return items.count()
    }

    inner class ViewHolder(val binding:CommunityListRowBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindItems(items: CommunityContent) {

            binding.titleArea.text = items.title
            binding.contentArea.text = items.content
            binding.timeArea.text = items.time

        }
        init{
            itemView.setOnClickListener{
                itemClickListener?.OnItemClick(items[adapterPosition])

            }
        }
    }
}