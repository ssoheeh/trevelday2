package com.example.travelday_2

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.travelday.TravelListAdapter
import com.example.travelday_2.TravelListManager.tripList
import com.example.travelday_2.databinding.FragmentTraveladdBinding


class TraveladdFragment : Fragment() {
    lateinit var binding:FragmentTraveladdBinding
    lateinit var adapter: TravelListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding= FragmentTraveladdBinding.inflate(layoutInflater)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        setBundle()
        initButton()
    }




    private fun setBundle() {
        val startDate = arguments?.getString("startDate")
        val endDate = arguments?.getString("endDate")
        var selectedCountry = arguments?.getString("country")
        if(selectedCountry!=null&&startDate!=null&&endDate!=null){

            val existingItem = tripList.find { it.country == selectedCountry && it.startDate == startDate && it.endDate == endDate }
            if (existingItem == null) {
                tripList.add(TravelListItem(selectedCountry, startDate, endDate))
            }
            for (i in 0..tripList.size - 1){
                tripList[i].country?.let { Log.i("여행목록", it) }
            }
            adapter.notifyDataSetChanged()
        }
    }



    private fun initButton() {
        binding.addTripButton.setOnClickListener {
            val CountryFragment=CountryFragment()
            parentFragmentManager.beginTransaction().apply{
                replace(R.id.frag_container,CountryFragment)
                addToBackStack(null)
                commit()
            }
        }
    }

    private fun initRecyclerView() {
        adapter = TravelListAdapter(tripList)
        binding.recyclerView.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL, false
        )
        binding.recyclerView.adapter = adapter
        adapter.itemClickListener = object : TravelListAdapter.OnItemClickListener {
            override fun OnItemClick(data: TravelListItem) {
                val bundle = Bundle().apply {
                    putSerializable("여행 클래스", data)
                }
                val dateListFragment = DateListItemFragment().apply {
                    arguments = bundle
                }
                parentFragmentManager.beginTransaction().apply {
                    replace(R.id.frag_container, dateListFragment)
                    addToBackStack("travelList")
                    commit()
                }


            }
        }
    }
}