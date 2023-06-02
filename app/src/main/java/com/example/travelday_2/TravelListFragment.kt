package com.example.travelday_2

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.travelday.TravelListAdapter
import com.example.travelday_2.databinding.FragmentTravellistBinding


class TravelListFragment : Fragment() {
    var tripList:ArrayList<TravelListItem> = ArrayList()
    lateinit var adapter: TravelListAdapter
    lateinit var selectedCountry:String
    lateinit var binding:FragmentTravellistBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTravellistBinding.inflate(layoutInflater)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.addTripButton.setOnClickListener {
            showCountryInputDialog()
        }
        initRecyclerView()
        if (arguments != null) {
            initBundle()
        }
    }

    private fun initBundle() {
        val startDate = arguments?.getString("startDate")
        val endDate=arguments?.getString("endDate")
        selectedCountry = arguments?.getString("여행국가") ?: ""  // selectedCountry 초기화
        tripList.add(TravelListItem(selectedCountry, startDate!!, endDate!!))
        adapter.notifyDataSetChanged()
    }


    private fun showCountryInputDialog() {

        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_country_input, null)
        val countryEditText: EditText = dialogView.findViewById(R.id.countryEditText)

        val dialogBuilder = AlertDialog.Builder(context)
            .setView(dialogView)
            .setTitle("여행할 국가")
            .setPositiveButton("저장", null)
            .setNegativeButton("취소") { dialog, _ ->
                dialog.dismiss()
            }

        val dialog = dialogBuilder.create()
        dialog.show()

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
            val countryName = countryEditText.text.toString().trim()
            if (countryName.isNotEmpty()) {
                selectedCountry=countryName
                dialog.dismiss()
                val bundle = Bundle().apply{
                    putString("여행국가",selectedCountry)
                }
                val newFragment=DatePickDialogFragment().apply{
                    arguments=bundle
                }
                parentFragmentManager.beginTransaction()
                    .replace(R.id.viewPager2,newFragment).commit()
            } else {
                countryEditText.error = "국가 이름을 입력하세요"
            }
        }
    }
    private fun initRecyclerView() {
        adapter= TravelListAdapter(tripList)
        binding.recyclerView.layoutManager = LinearLayoutManager(context,
            LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.adapter = adapter
        adapter.itemClickListener=object:TravelListAdapter.OnItemClickListener{
            override fun OnItemClick(data: TravelListItem) {
                val bundle = Bundle().apply{
                    putParcelable("여행 클래스",data)
                }
                val newFragment=DateListFragment().apply{
                    arguments=bundle
                }
                parentFragmentManager.beginTransaction()
                    .replace(R.id.viewPager2,newFragment).commit()

            }
        }
    }

}