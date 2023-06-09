package com.example.travelday_2

data class CommunityPost(
    val title: String = "",
    val content: String = "",
    val time: String = "",
    val imageUrls: MutableList<String> = mutableListOf(),
    val comments: MutableMap<String, Comment> = mutableMapOf() // 변경된 부분
)


