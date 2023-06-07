package com.example.travelday_2

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
        //관찰
        sharedViewModel.countryList.observe(viewLifecycleOwner) { countryList ->
            adapter.notifyDataSetChanged()
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

    @SuppressLint("SuspiciousIndentation", "SetTextI18n")
    private fun initRecyclerView() {
        val country = arguments?.getSerializable("클릭된 국가") as SharedViewModel.Country
        if (country != null) {
                adapter = DateListAdapter(requireContext(), country.dateList)
                binding.recyclerView.adapter = adapter
                binding.recyclerView.layoutManager = LinearLayoutManager(context,
                    LinearLayoutManager.VERTICAL,false)}
                adapter.itemClickListener=object:DateListAdapter.OnItemClickListener{
                    override fun onItemClick(data: SharedViewModel.Date) {
                        val bundle = Bundle().apply {
                            putSerializable("클릭된 국가", country)
                            putSerializable("클릭된 날짜", data)
                        }
                        val dailyAddFragment=DailyAddFragment().apply {
                            arguments=bundle
                        }
                        parentFragmentManager.beginTransaction().apply {
                            add(R.id.frag_container, dailyAddFragment)
                            hide(this@DateListFragment)
                            addToBackStack(null)
                            commit()
                        }
                    }
                }
        //상단 탭에 국가이름, 날짜 데이터 및 디데이 표시
        val startDate = country.dateList.firstOrNull()?.date
        val endDate = country.dateList.lastOrNull()?.date
        val travelPeriod = if (startDate != null && endDate != null) {
            "$startDate ~ $endDate"
        } else {
            ""
        }
        binding.travelData.text=country.name +"\n " +travelPeriod
        binding.dDayDateList.text="D-"+country.dDay

       // swipe 시 remove기능 구현
        val simpleCallback=object: ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN, ItemTouchHelper.RIGHT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                adapter.moveItem(viewHolder.adapterPosition, target.adapterPosition)
                return true
            }
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                adapter.removeItem(viewHolder.adapterPosition)
            }
        }
        val itemTouchHelper= ItemTouchHelper(simpleCallback)
        itemTouchHelper.attachToRecyclerView(binding.recyclerView)
    }
}



