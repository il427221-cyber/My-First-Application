package ru.netology.nmedia.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.netology.nmedia.dto.Post

@Entity(tableName = "posts")
data class PostEntity(
    @PrimaryKey(true)
    val id: Long,
    val author: String,
    val published: String,
    val content: String,
    val likes: Int,
    val reposts: Int,
    var video: String?,
    val likedByMe: Boolean,
    val repostedByMe: Boolean
) {
    fun toPost() = Post(
        id = id,
        author = author,
        published = published,
        content = content,
        likes = likes,
        reposts = reposts,
        video = video,
        likedByMe = likedByMe,
        repostedByMe = repostedByMe

    )

    companion object {
        fun fromPost(post: Post): PostEntity = with(post) {
            PostEntity(
                id = id,
                author = author,
                published = published,
                content = content,
                likes = likes,
                reposts = reposts,
                video = video,
                likedByMe = likedByMe,
                repostedByMe = repostedByMe


            )
        }
    }
}
