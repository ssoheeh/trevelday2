package com.example.travelday_2

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.travelday_2.databinding.FragmentDateListParentBinding
import java.util.*

class DateListParentFragment : Fragment() {
lateinit var binding:FragmentDateListParentBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentDateListParentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setFragment()
    }

    fun setFragment() {
        var selectedTravelItem :TravelListItem?= arguments?.getSerializable("여행 클래스") as TravelListItem
        val bundle=Bundle().apply {
            putSerializable("여행 클래스",selectedTravelItem)
        }
        val dateListItem = DateListItemFragment().apply {
            arguments = bundle
        }
        parentFragmentManager.beginTransaction().apply {
            replace(R.id.frag_container_child,dateListItem )
            addToBackStack(null)
            commit()
        }


    }



}