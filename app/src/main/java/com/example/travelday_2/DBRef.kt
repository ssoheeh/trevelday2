package com.example.travelday_2

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
//데이터 베이스 참조 클래스
class DBRef {
    companion object {
        private val database = Firebase.database

        val contentRef = database.getReference("content")
    }
}