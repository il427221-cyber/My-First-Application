package ru.netology.nmedia.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.netology.nmedia.dto.Attachment
import ru.netology.nmedia.dto.Post

@Entity(tableName = "posts")
data class PostEntity(
    @PrimaryKey(true)
    val id: Long,
    val author: String,
    val authorAvatar: String?,
    val published: Long,
    val content: String,
    val likes: Int,
    val likedByMe: Boolean,

) {
    fun toPost() = Post(
        id = id,
        author = author,
        authorAvatar = authorAvatar,
        published = published,
        content = content,
        likes = likes,
        likedByMe = likedByMe,


    )

    companion object {
        fun fromPost(post: Post): PostEntity = with(post) {
            PostEntity(
                id = id,
                author = author,
                authorAvatar = authorAvatar,
                content = content,
                published = published,
                likedByMe = likedByMe,
                likes = likes,

            )
        }
    }
}
