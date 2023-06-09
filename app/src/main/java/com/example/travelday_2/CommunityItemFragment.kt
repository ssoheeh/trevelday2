package com.example.travelday_2

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.travelday_2.databinding.FragmentCommunityItemBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener


class CommunityItemFragment : Fragment() {
    lateinit var binding:FragmentCommunityItemBinding
    lateinit var contentAdapter: CommentAdapter
    private val commentList = mutableListOf<Comment>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentCommunityItemBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initLayout()
        initBackStack()
    }




    private fun initLayout() {
        val key = arguments?.getString("key")
        contentAdapter = CommentAdapter(commentList)
        // RecyclerView의 adapter에 ContentAdapter를 설정한다.
        binding.commentRecyclerView.adapter = contentAdapter
        // layoutManager 설정
        // LinearLayoutManager을 사용하여 수직으로 아이템을 배치한다.
        binding.commentRecyclerView.layoutManager = LinearLayoutManager(context,
            LinearLayoutManager.VERTICAL,false)
        getFBContentData()
        binding.commentButton.setOnClickListener {
            parentFragmentManager.beginTransaction().apply {
                val commentWriteFragment = CommentWriteFragment().apply {
                    arguments = Bundle().apply {
                        putString("key", key) // Pass the key to CommunityItemFragment
                    }
                }
                add(R.id.frag_container_community, commentWriteFragment)
                hide(this@CommunityItemFragment)
                addToBackStack(null)
                commit()
            }
        }
    }
    private fun getFBContentData() {
        val postKey = arguments?.getString("key")
        if (postKey == null) {
            Toast.makeText(context, "Invalid post", Toast.LENGTH_SHORT).show()
            return
        }

        val postListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                commentList.clear()

                for (data in snapshot.child("comments").children) {
                    val item = data.getValue(Comment::class.java)
                    Log.d("ContentListActivity", "item: ${item}")
                    commentList.add(item!!)
                }
                commentList.reverse()
                contentAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
            }
        }
        FBRef.contentRef.child(postKey).addValueEventListener(postListener)
    }

    private fun initBackStack() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                parentFragmentManager.popBackStack()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }
}