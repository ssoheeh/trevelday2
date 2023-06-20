package com.example.travelday_2

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
//데이터 베이스 참조 클래스
class DBRef {
    companion object {
        private val database = Firebase.database

        val contentRef = database.getReference("content")
        val userRef = database.getReference("users")
        fun writeDataToDatabase(userId: String, country: String) {
            val countryRef = userRef.child(userId).child(country)
            countryRef.setValue(country)
        }

        fun writeDataToDatabase(userId: String, country: String, date: String) {
            val dateRef = userRef.child(userId).child(country).child(date)
            dateRef.setValue(date)
        }

        fun writeDataToDatabase(userId: String, country: String, date: String, time: String, task: String, color: String) {
            val taskRef = userRef.child(userId).child(country).child(date).child("dailySchedule").child("tasklist").push()
            taskRef.child("time").setValue(time)
            taskRef.child("task").setValue(task)
            taskRef.child("color").setValue(color)
        }

        fun readDataFromDatabase(userId: String, country: String, callback: (ArrayList<String>) -> Unit) {
            val dateListRef = userRef.child(userId).child(country).child("dateList")
            dateListRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val dates = ArrayList<String>()
                    snapshot.children.forEach { dateSnapshot ->
                        val date = dateSnapshot.getValue(String::class.java)
                        if (date != null) {
                            dates.add(date)
                        }
                    }
                    callback(dates)
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle error here
                    callback(ArrayList())
                }
            })
        }

        fun readDataFromDatabase(userId: String) {

            // userId로 데이터 읽기
            val userCountryRef = userRef.child(userId)
            userCountryRef.get().addOnSuccessListener {
                println("User's country read successfully: ${it.value}")
            }.addOnFailureListener{
                println("Failed to read user's country.")
            }
        }

        fun readDataFromDatabase(userId: String, country: String) {

            // userId -> country로 데이터 읽기
            val userDateRef = userRef.child(userId).child(country)
            userDateRef.get().addOnSuccessListener {
                println("User's dates read successfully: ${it.value}")
            }.addOnFailureListener{
                println("Failed to read user's dates.")
            }
        }

        fun readDataFromDatabase(userId: String, country: String, date: String) {
            val userScheduleRef = userRef.child(userId).child(country).child(date)
            userScheduleRef.get().addOnSuccessListener { snapshot ->
                val dailyItemList = ArrayList<DailyItem>()
                snapshot.children.forEach { scheduleSnapshot ->
                    val schedule = scheduleSnapshot.getValue(DailyItem::class.java)
                    schedule?.let { dailyItemList.add(it) }
                }
                // dailyItemList을 사용하거나 반환하도록 처리
                // 예를 들어 이 리스트를 RecyclerView의 어댑터에 전달하거나 다른 곳에서 사용할 수 있습니다.
            }.addOnFailureListener {
                println("Failed to read user's schedules.")
            }
        }
    }

}