package com.example.travelday_2

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import com.example.travelday_2.databinding.FragmentCommunityWriteBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class CommunityWriteFragment : Fragment() {

    lateinit var binding: FragmentCommunityWriteBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentCommunityWriteBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initLayout()
        initBackStack()
    }

    fun initLayout(){
        binding.writeBtn.setOnClickListener {
            val title = binding.titleArea.text.toString()
            val content = binding.contentArea.text.toString()
            val time = getTime()

            // push()는 목록을 만들어주며 랜덤한 문자열을 할당한다.
            val key = FBRef.contentRef.push().key.toString()

            // child()는 해당 키 위치로 이동하는 메서드로 child()를 사용하여 key 값의 하위에 값을 저장한다.
            // setValue() 메서드를 사용하여 값을 저장한다.
            FBRef.contentRef
                .child(key)
                .setValue(CommunityContent(title, content, time))

            Toast.makeText(context, "게시글 입력 완료", Toast.LENGTH_SHORT).show()
            parentFragmentManager.popBackStack()
        }


    }
    fun getTime(): String {
        val currentDateTime = Calendar.getInstance().time
        val dateFormat =
            SimpleDateFormat("yyyy.MM.dd HH:mm:ss", Locale.KOREA).format(currentDateTime)
        return dateFormat
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

