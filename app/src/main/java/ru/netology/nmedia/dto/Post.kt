package ru.netology.nmedia.dto

data class Post(
    val id: Long = 0,
    val author: String = " ",
    val content: String = " ",
    val published: Long = 0,
    var likedByMe: Boolean = false,
    var likes: Int = 0
)
