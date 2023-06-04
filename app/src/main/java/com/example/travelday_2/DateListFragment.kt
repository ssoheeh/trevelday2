package com.example.travelday_2

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.travelday_2.databinding.FragmentDateListBinding
import java.util.*


class DateListFragment : Fragment() {
    lateinit var binding:FragmentDateListBinding
    lateinit var adapter: DateListAdapter
    private val sharedViewModel: SharedViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding= FragmentDateListBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        initBackStack()
    }
    private fun initBackStack() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // 뒤로가기 버튼이 눌렸을 때 처리할 동작 구현
                parentFragmentManager.popBackStack()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }




    @SuppressLint("SuspiciousIndentation", "SetTextI18n")
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
                            add(R.id.frag_container, dailyFragment)
                            hide(this@DateListFragment)
                            addToBackStack(null)
                            commit()
                        }
                    }
                }
        //국가이름, 날짜 데이터 및 디데이 표시
        val startDate = country.dateList.firstOrNull()?.date
        val endDate = country.dateList.lastOrNull()?.date
        val travelPeriod = if (startDate != null && endDate != null) {
            "$startDate ~ $endDate"
        } else {
            ""
        }
        binding.travelData.text=country.name +"\n " +travelPeriod
        binding.dDayDateList.text="D-"+country.dDay

        }

}

