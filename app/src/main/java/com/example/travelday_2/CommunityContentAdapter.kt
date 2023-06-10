package com.example.travelday_2

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.travelday_2.databinding.CommunityListRowBinding

class CommunityContentAdapter(val items: MutableList<CommunityPost>,val keys: MutableList<String>
) :
    RecyclerView.Adapter<CommunityContentAdapter.ViewHolder>() {

    interface OnItemClickListener{
        fun OnItemClick(data:CommunityPost, key:String)
    }
    var itemClickListener: CommunityContentAdapter.OnItemClickListener?=null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommunityContentAdapter.ViewHolder {
        val view = CommunityListRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: CommunityContentAdapter.ViewHolder, position: Int) {
        holder.bindItems(items[position])
    }
    override fun getItemCount(): Int {
        return items.count()
    }

    inner class ViewHolder(val binding:CommunityListRowBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindItems(items: CommunityPost) {

            binding.titleArea.text = items.title
            binding.contentArea.text = items.content
            binding.timeArea.text = items.time

        }
        init{
            itemView.setOnClickListener{
                itemClickListener?.OnItemClick(items[adapterPosition],keys[adapterPosition])

            }
        }
    }
}