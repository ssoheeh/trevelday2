package com.example.travelday_2

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.travelday_2.databinding.FragmentTravellistParentBinding


class TravelListParentFragment : Fragment() {

    lateinit var binding:FragmentTravellistParentBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTravellistParentBinding.inflate(layoutInflater)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initLayout()
    }

    @SuppressLint("SuspiciousIndentation")
    fun initLayout() {
        val travelList = TravelListFragment()
            childFragmentManager.beginTransaction()
                .add(R.id.frag_container, travelList)
                .commit()
        }
    }
