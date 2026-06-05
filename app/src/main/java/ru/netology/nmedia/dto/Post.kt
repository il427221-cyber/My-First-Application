package ru.netology.nmedia.dto

data class Post(
    val id: Long = 0,
    val author: String = " ",
    var authorAvatar: String? = null,
    val content: String = " ",
    val published: Long = 0,
    var likedByMe: Boolean = false,
    var likes: Int = 0,
    var attachment: Attachment? = null
)

data class Attachment(
    val url: String,
    val description: String?,
    val type: AttachmentType,
)