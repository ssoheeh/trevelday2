package com.example.travelday_2

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.travelday_2.databinding.FragmentCommunityItemBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.values


class CommunityItemFragment : Fragment() {
    lateinit var binding:FragmentCommunityItemBinding
    lateinit var commentAdapter: CommentAdapter
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
        initLikeCount()
        initCommentCount()
    }

    private fun initLikeCount() {
        val postKey = arguments?.getString("key")
        if (postKey != null) {
            val postRef = DBRef.contentRef.child(postKey)
            // ValueEventListener를 설정하여 likeList에 변화가 생길 때마다 UI를 업데이트합니다.
            postRef.child("likeList").addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val likeList = snapshot.value as? MutableList<String> ?: mutableListOf()
                    // 변환된 좋아요 수를 TextView에 표시합니다.
                    binding.likeCount.text = likeList.size.toString()
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(context, "Failed to get like count.", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    private fun initCommentCount() {
        val postKey = arguments?.getString("key")
        if (postKey != null) {
            val postRef = DBRef.contentRef.child(postKey)
            // ValueEventListener를 설정하여 likeList에 변화가 생길 때마다 UI를 업데이트합니다.
            postRef.child("comments").addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val comments = snapshot.value as? Map<String, Comment> ?: mapOf()
                    binding.commentCount.text = comments.size.toString()
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(context, "Failed to get like count.", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
    private fun initLayout() {
        commentAdapter = CommentAdapter(commentList)
        binding.commentRecyclerView.adapter = commentAdapter
        binding.commentRecyclerView.layoutManager = LinearLayoutManager(context,
            LinearLayoutManager.VERTICAL,false)


        val key = arguments?.getString("key")
        //글 작성자, 내용 데이터 세팅
        val postRef = key?.let { DBRef.contentRef.child(it) }

        postRef?.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // CommunityPost 객체로 데이터를 변환
                val post = snapshot.getValue(CommunityPost::class.java)
                // 데이터 바인딩
                binding.usernameItem.text = post?.userId
                //binding.titleItem.text = post?.title 제목 추가시 변경
                binding.contentItem.text = post?.content
                binding.timeItem.text = post?.time
            }

            override fun onCancelled(error: DatabaseError) {
                // Log a message or handle the error.
            }
        })

        //이미지 데이터 세팅
        val imageRef = key?.let { DBRef.contentRef.child(it).child("imageUrl") }

        imageRef?.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val imageUrl = snapshot.getValue(String::class.java)
                Glide.with(this@CommunityItemFragment).load(imageUrl).into(binding.photo)

            }

            override fun onCancelled(error: DatabaseError) {
                // Log a message or handle the error.
            }
        })

        getCommentData() //댓글  업데이트

        //좋아요 버튼
        binding.likeButton.setOnClickListener {
            val user = FirebaseAuth.getInstance().currentUser
            val userId = user?.email ?: "Anonymous"

            val postKey = arguments?.getString("key")

            if (postKey != null) {
                val postRef = DBRef.contentRef.child(postKey)
                postRef.child("likeList").addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val likeList = snapshot.value as? MutableList<String> ?: mutableListOf()
                        if (!likeList.contains(userId)) {
                            likeList.add(userId)
                            postRef.child("likeList").setValue(likeList)
                            Toast.makeText(context, "좋아요", Toast.LENGTH_SHORT).show()
                        } else {
                            likeList.remove(userId)
                            postRef.child("likeList").setValue(likeList)
                            Toast.makeText(context, "좋아요 취소", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(context, "Failed to get like count.", Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }
        //댓글 추가
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

    private fun getCommentData() {
        
        //게시물 key값 null check
        val postKey = arguments?.getString("key")
        if (postKey == null) {
            Toast.makeText(context, "Invalid post", Toast.LENGTH_SHORT).show()
            return
        }
        //댓글 업데이트 구현
        val postListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                commentList.clear()

                for (data in snapshot.child("comments").children) {
                    val item = data.getValue(Comment::class.java)
                    commentList.add(item!!)
                }
                commentList.reverse()
                commentAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
            }
        }
        DBRef.contentRef.child(postKey).addValueEventListener(postListener)
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