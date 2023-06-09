package com.example.travelday_2


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.travelday_2.databinding.CommentListRowBinding


class CommentAdapter(val items: MutableList<Comment>) :
    RecyclerView.Adapter<CommentAdapter.ViewHolder>() {

    interface OnItemClickListener{
        fun OnItemClick(data:Comment)
    }
    var itemClickListener: CommentAdapter.OnItemClickListener?=null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentAdapter.ViewHolder {
        val view = CommentListRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    // 생성된 View Holder에 데이터를 바인딩 해주는 메서드
    override fun onBindViewHolder(holder: CommentAdapter.ViewHolder, position: Int) {
        holder.bindItems(items[position])
    }
    override fun getItemCount(): Int {
        return items.count()
    }

    inner class ViewHolder(val binding:CommentListRowBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindItems(items: Comment) {

            binding.username.text = items.author
            binding.content.text = items.content
            binding.time.text = items.time

        }
        init{
            itemView.setOnClickListener{
                itemClickListener?.OnItemClick(items[adapterPosition])

            }
        }
    }
}