package com.example.travelday_2

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.travelday_2.databinding.FragmentCommunityMainBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener


class CommunityMainFragment : Fragment() {
    lateinit var binding: FragmentCommunityMainBinding
    lateinit var contentAdapter: CommunityContentAdapter
    private val contentList = mutableListOf<CommunityPost>()
    private val keysList = mutableListOf<String>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentCommunityMainBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initLayout()

    }

    private fun initLayout() {
        contentAdapter = CommunityContentAdapter(contentList,keysList)
        binding.recyclerview.adapter = contentAdapter
        binding.recyclerview.layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)

        binding.contentWriteBtn.setOnClickListener {
            parentFragmentManager.beginTransaction().apply {
                val communityWriteFragment=CommunityWriteFragment()
                add(R.id.frag_container_community, communityWriteFragment)
                hide(this@CommunityMainFragment)
                addToBackStack(null)
                commit()
            }
        }

        contentAdapter.itemClickListener = object:CommunityContentAdapter.OnItemClickListener{
            override fun OnItemClick(data: CommunityPost, key: String) { // key added here
                parentFragmentManager.beginTransaction().apply {
                    val communityItemFragment = CommunityItemFragment().apply {
                        arguments = Bundle().apply {
                            putString("key", key) // Pass the key to CommunityItemFragment
                        }
                    }
                    add(R.id.frag_container_community, communityItemFragment)
                    hide(this@CommunityMainFragment)
                    addToBackStack(null)
                    commit()
                }
            }
        }

        // 데이터베이스에서 데이터 읽어오기
        getFBContentData()
    }

    private fun getFBContentData() {
        val postListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                contentList.clear()

                for (data in snapshot.children) {
                    val item = data.getValue(CommunityPost::class.java)
                    Log.d("ContentListActivity", "item: ${item}")
                    // 리스트에 읽어 온 데이터를 넣어준다.
                    contentList.add(item!!)
                    keysList.add(data.key!!)
                }
                contentList.reverse()
                keysList.reverse()
                contentAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
            }
        }
        // addValueEventListener() 메서드로 DatabaseReference에 ValueEventListener를 추가
        FBRef.contentRef.addValueEventListener(postListener)
    }


}