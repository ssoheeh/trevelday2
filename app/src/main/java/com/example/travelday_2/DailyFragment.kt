package com.example.travelday_2

import android.app.TimePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TimePicker
import androidx.activity.OnBackPressedCallback
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.travelday_2.databinding.FragmentDailyBinding
import java.util.ArrayList
import java.util.Calendar


class DailyFragment : Fragment() {
    lateinit var binding:FragmentDailyBinding
    private var dailyList: ArrayList<DailyItem> = ArrayList()
    lateinit var adapter:DailyScheduleAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentDailyBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        initBackStack()
        initButton()
    }

    private fun initButton() {
        binding.addButton.setOnClickListener {
            val dailyAddFragment=DailyAddFragment()
            parentFragmentManager.beginTransaction().apply{
                replace(R.id.frag_container,dailyAddFragment)
                addToBackStack(null)
                commit()
            }
        }
    }

    private fun initBackStack() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (parentFragmentManager.backStackEntryCount > 0) {
                    parentFragmentManager.popBackStack()
                } else {
                    requireActivity().finish()
                }
            }
        })
    }

    private fun initRecyclerView() {
        for(i in 0..24){
            dailyList.add(DailyItem(i.toString(),""))
        }
        adapter = DailyScheduleAdapter(dailyList)
        binding.scheduleRecyclerView.adapter = adapter
        binding.scheduleRecyclerView.layoutManager = LinearLayoutManager(context,
            LinearLayoutManager.VERTICAL,false)
    }
}