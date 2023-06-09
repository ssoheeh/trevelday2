package com.example.travelday_2

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.travelday_2.databinding.FragmentCommunityMainBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener


class CommunityMainFragment : Fragment() {
    lateinit var binding: FragmentCommunityMainBinding
    lateinit var contentAdapter: CommunityContentAdapter
    private val contentList = mutableListOf<CommunityContent>()
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
        contentAdapter = CommunityContentAdapter(contentList)
        // RecyclerView의 adapter에 ContentAdapter를 설정한다.
        binding.recyclerview.adapter = contentAdapter
        // layoutManager 설정
        // LinearLayoutManager을 사용하여 수직으로 아이템을 배치한다.
        binding.recyclerview.layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)

        // 글쓰기 버튼을 클릭 했을 경우 ContentWriteActivity로 이동한다.
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
            override fun OnItemClick(data: CommunityContent) {
                Toast.makeText(context,data.content,Toast.LENGTH_SHORT).show()
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
                    val item = data.getValue(CommunityContent::class.java)
                    Log.d("ContentListActivity", "item: ${item}")
                    // 리스트에 읽어 온 데이터를 넣어준다.
                    contentList.add(item!!)
                }
                contentList.reverse()
                // notifyDataSetChanged()를 호출하여 adapter에게 값이 변경 되었음을 알려준다.
                contentAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
            }
        }
        // addValueEventListener() 메서드로 DatabaseReference에 ValueEventListener를 추가한다.
        FBRef.contentRef.addValueEventListener(postListener)
    }


}