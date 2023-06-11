package com.example.travelday_2

import android.app.AlertDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.travelday_2.databinding.FragmentDailyAddBinding
import java.util.*

class DailyScheduleAddFragment : Fragment() {
    lateinit var binding: FragmentDailyAddBinding
    val sharedViewModel: SharedViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDailyAddBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initLayout()

    }



    private fun initLayout() {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)
        val colors = arrayOf("#ADFF2F", "#FF0000", "#0000FF","#FFA500","#9370DB")

        val timeSetListener = TimePickerDialog.OnTimeSetListener { _, selectedHour, selectedMinute ->
            val builder = AlertDialog.Builder(context)
            val inflater = LayoutInflater.from(context)
            val dialogView = inflater.inflate(R.layout.fragment_daily_add, null)

            val taskEditText = dialogView.findViewById<EditText>(R.id.taskEditText)

            builder.setView(dialogView)

            builder.setPositiveButton("OK") { _, _ ->

                //토스트메시지
                val msg = "$selectedHour:${String.format("%02d", selectedMinute)} ${taskEditText.text} 추가되었습니다"
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()


                val task = taskEditText.text.toString()
                val selectedCountry = arguments?.getSerializable("클릭된 국가") as SharedViewModel.Country
                val selectedDate = arguments?.getSerializable("클릭된 날짜") as SharedViewModel.Date
                val countryList = sharedViewModel.countryList.value
                val countryIndex = countryList?.indexOf(selectedCountry)
                val dateIndex = selectedCountry.dateList.indexOf(selectedDate)

                if (countryIndex != null && dateIndex != -1) {
                    val scheduleCount = sharedViewModel.getScheduleCount(countryIndex, dateIndex)
                    val color = colors[scheduleCount % colors.size]
                    sharedViewModel.addDailySchedule(countryIndex, dateIndex, selectedHour, selectedMinute, task, color)
                }
                parentFragmentManager.popBackStack()
            }

            builder.setNegativeButton("Cancel", null)
            builder.show()
        }

        val timePickerDialog = TimePickerDialog(context, timeSetListener, hour, minute, true)
        timePickerDialog.show()
    }


}




