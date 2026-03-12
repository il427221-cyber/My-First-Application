package ru.netology.nmedia.dto

data class Post (
    val id: Int,
    val author: String,
    val published: String,
    val content: String,
    var likes: Int = 999,
    var reposts: Int = 5,
    var likedByMe: Boolean = false,
    var repostedByMe: Boolean = false
)
