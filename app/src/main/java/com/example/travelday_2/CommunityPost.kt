package com.example.travelday_2

data class CommunityPost(
    val userId:String = "",
    val title: String = "",
    val content: String = "",
    val time: String = "",
    val likeList: MutableList<String> = mutableListOf(),
    val imageUrls: MutableList<String> = mutableListOf(),
    val comments: MutableMap<String, Comment> = mutableMapOf()
)


