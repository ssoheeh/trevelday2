package com.example.travelday_2

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.travelday.TravelListAdapter
import com.example.travelday_2.databinding.FragmentDateListItemBinding
import java.text.SimpleDateFormat
import java.util.*


class DateListItemFragment : Fragment() {
    lateinit var binding:FragmentDateListItemBinding
    lateinit var adapter: DateListAdapter
    private val sharedViewModel: SharedViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding= FragmentDateListItemBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        initBackStack()
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


    @SuppressLint("SuspiciousIndentation")
    private fun initRecyclerView() {

        val country = arguments?.getSerializable("클릭된 국가") as SharedViewModel.Country
        if (country != null) {
                adapter = DateListAdapter(country.dateList)
                binding.recyclerView.adapter = adapter
                binding.recyclerView.layoutManager = LinearLayoutManager(context,
                    LinearLayoutManager.VERTICAL,false)}

                adapter.itemClickListener=object:DateListAdapter.OnItemClickListener{
                    override fun onItemClick(data: SharedViewModel.Date) {

                        val bundle = Bundle().apply {
                            putSerializable("클릭된 국가", country)
                            putSerializable("클릭된 날짜", data)
                        }
                        val dailyFragment=DailyFragment().apply {
                            arguments=bundle
                        }
                        parentFragmentManager.beginTransaction().apply {
                            replace(R.id.frag_container,dailyFragment )
                            addToBackStack(null)
                            commit()
                        }
                    }
                }


        }

}

