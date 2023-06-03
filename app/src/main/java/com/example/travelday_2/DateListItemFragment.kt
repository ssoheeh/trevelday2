package com.example.travelday_2

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.travelday.TravelListAdapter
import com.example.travelday_2.databinding.FragmentDateListItemBinding
import java.text.SimpleDateFormat
import java.util.*


class DateListItemFragment : Fragment() {
    lateinit var binding:FragmentDateListItemBinding
    lateinit var adapter: DateListAdapter
    var dateList:ArrayList<DateListItem> = ArrayList()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding= FragmentDateListItemBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getTraveldata()
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


    private fun getTraveldata() {

            var selectedTravelItem :TravelListItem?= arguments?.getSerializable("여행 클래스") as TravelListItem
            if (selectedTravelItem != null) {
                val startDate = convertStringToDate(selectedTravelItem.startDate!!)
                val endDate = convertStringToDate(selectedTravelItem.endDate!!)
                getDatesBetween(startDate, endDate)
                adapter = DateListAdapter(dateList)
                binding.recyclerView.adapter = adapter
                binding.recyclerView.layoutManager = LinearLayoutManager(context,
                    LinearLayoutManager.VERTICAL,false)

                adapter.itemClickListener=object:DateListAdapter.OnItemClickListener{
                    override fun onItemClick(data: DateListItem) {
                        val dailyFragment=DailyFragment()
                        parentFragmentManager.beginTransaction().apply {
                            replace(R.id.frag_container,dailyFragment )
                            addToBackStack(null)
                            commit()
                        }
                    }
                }

            }
        }

        private fun convertStringToDate(dateString: String): Date {
            val format = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault())
            return format.parse(dateString) ?: Date()
        }

        private fun getDatesBetween(startDate: Date, endDate: Date) {
            val dates = mutableListOf<Date>()
            val calendar = Calendar.getInstance()
            calendar.time = startDate

            while (calendar.time.before(endDate)|| calendar.time.equals(endDate)) {
                val result = calendar.time
                dateList.add(DateListItem(result))
                calendar.add(Calendar.DATE, 1)
            }


        }
}

