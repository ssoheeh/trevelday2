package com.example.travelday_2

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.travelday_2.databinding.FragmentDailyAddBinding
import java.util.*

class DailyAddFragment : Fragment() {
    lateinit var binding: FragmentDailyAddBinding

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
    @SuppressLint("MissingInflatedId")
    private fun initLayout() {
            // Custom Dialog 생성
            val builder = AlertDialog.Builder(context)
            val inflater = this.layoutInflater
            val dialogView = inflater.inflate(R.layout.fragment_daily_add, null)

            // NumberPicker에 24시간 설정
            val np = dialogView.findViewById<NumberPicker>(R.id.hourPicker)
            np.maxValue = 23
            np.minValue = 0

            // EditText 찾기
            val taskEditText = dialogView.findViewById<EditText>(R.id.taskEditText)

            // NumberPicker 현재 시간으로 초기화
            val calendar = Calendar.getInstance()
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            np.value = hour

            // AlertDialog Builder 사용
            builder.setView(dialogView)
            builder.setPositiveButton("OK") { _, _ ->
                val msg = "You selected ${np.value}:00, Task: ${taskEditText.text}"
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
            }
            builder.setNegativeButton("Cancel", null)
            builder.show()
        }
}


