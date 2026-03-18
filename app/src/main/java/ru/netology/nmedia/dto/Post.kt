package ru.netology.nmedia.dto

data class Post(
    val id: Long,
    val author: String,
    val published: String,
    val content: String,
    val likes: Int = 999,
    val reposts: Int = 5,
    val likedByMe: Boolean = false,
    val repostedByMe: Boolean = false
)
