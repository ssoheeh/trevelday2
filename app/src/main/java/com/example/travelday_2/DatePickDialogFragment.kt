package com.example.travelday_2

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.travelday_2.databinding.FragmentDatePickDialogBinding
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.*


class DatePickDialogFragment : Fragment() {
    lateinit var binding:FragmentDatePickDialogBinding
    var selectedCountry: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentDatePickDialogBinding.inflate(layoutInflater)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        selectedCountry = arguments?.getString("여행국가")
        showDataRangePicker()
    }


    private fun showDataRangePicker() {

        val dateRangePicker =
            MaterialDatePicker
                .Builder.dateRangePicker()
                .setTitleText("Select Date")
                .build()

        dateRangePicker.show(
            activity?.supportFragmentManager!!,
            "date_range_picker"
        )

        dateRangePicker.addOnPositiveButtonClickListener { dateSelected ->

            val startDate = dateSelected.first
            val endDate = dateSelected.second
            val bundle=Bundle().apply {
                putString("startDate",convertLongToTime(startDate))
                putString("endDate",convertLongToTime(endDate))
                putString("여행국가",selectedCountry)
            }
            val newFragment=TravelListFragment().apply {
                arguments=bundle
            }
            activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.fragment_container,newFragment)?.addToBackStack(null)?.commit()
        }

    }


    private fun convertLongToTime(time: Long): String {
        val date = Date(time)
        val format = SimpleDateFormat(
            "yyyy.MM.dd",
            Locale.getDefault())
        return format.format(date)
    }

}