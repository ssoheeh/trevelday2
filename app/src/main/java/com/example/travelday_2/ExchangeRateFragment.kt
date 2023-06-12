package com.example.travelday_2

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.travelday_2.databinding.FragmentDateListBinding
import com.example.travelday_2.databinding.FragmentExchangeRateBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*
import java.util.concurrent.ExecutionException


class ExchangeRateFragment : Fragment() {

    lateinit var binding:FragmentExchangeRateBinding
    private val currencyList = arrayOf("KRW", "USD", "EUR", "CAD")
    private lateinit var et_from: TextView
    private lateinit var tv_to: TextView
    private lateinit var btn_exchange: Button
    private val fromto = arrayOfNulls<String>(2)
    private lateinit var tv_test: TextView
    private var currencyRate = 0.0

    //lateinit var adapter: DateListAdapter
    private val sharedViewModel: SharedViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding= FragmentExchangeRateBinding.inflate(layoutInflater)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        //initRecyclerView()
        initBackStack()
        //관찰
//        sharedViewModel.countryList.observe(viewLifecycleOwner) { countryList ->
//            adapter.notifyDataSetChanged()
//        }
    }

    private fun init(){
        val selectedCountry = arguments?.getSerializable("클릭된 국가") as SharedViewModel.Country
        //val selectedDate = arguments?.getSerializable("클릭된 날짜") as SharedViewModel.Date
        val startDate = selectedCountry.dateList.firstOrNull()?.date
        val endDate = selectedCountry.dateList.lastOrNull()?.date
        val travelPeriod = if (startDate != null && endDate != null) {
            "$startDate ~ $endDate"
        } else {
            ""
        }
        binding.travelData.text=selectedCountry.name +"\n " +travelPeriod
        binding.dDayDateList.text="D-"+selectedCountry.dDay
        binding.weatherLayout.setOnClickListener {

        }
        binding.exchangeLayout.setOnClickListener {

        }

        val spinner = binding.spinner
        val spinner2 = binding.spinner2

        et_from = binding.etFrom
        tv_to = binding.tvTo

        btn_exchange = binding.btnExchange

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, currencyList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View?, i: Int, l: Long) {
                fromto[0] = currencyList[i]
            }

            override fun onNothingSelected(adapterView: AdapterView<*>) {

            }
        }

        spinner2.adapter = adapter
        spinner2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View?, i: Int, l: Long) {
                fromto[1] = currencyList[i]
            }

            override fun onNothingSelected(adapterView: AdapterView<*>) {

            }
        }

        btn_exchange.setOnClickListener {
            GlobalScope.launch {
                try {
                    val task = Task()
                    currencyRate = task.executeAsync(*fromto)

                } catch (e: InterruptedException) {
                    e.printStackTrace()
                } catch (e: ExecutionException) {
                    e.printStackTrace()
                }

                val input = et_from.text.toString().toDouble()
                val result = Math.round(input * currencyRate * 100.0) / 100.0

                tv_to.text = result.toString()
            }

        }
    }

    // 뒤로가기 버튼이 눌렸을 때 처리할 동작 구현
    private fun initBackStack() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                parentFragmentManager.popBackStack()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }


}



