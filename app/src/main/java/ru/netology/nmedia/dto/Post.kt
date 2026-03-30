package ru.netology.nmedia.dto

import java.net.URL

data class Post(
    val id: Long = 0,
    val author: String = "",
    val published: String = "",
    val content: String = "",
    val likes: Int = 0,
    val reposts: Int = 5,
    var video: String? = null,
    val likedByMe: Boolean = false,
    val repostedByMe: Boolean = false
)
