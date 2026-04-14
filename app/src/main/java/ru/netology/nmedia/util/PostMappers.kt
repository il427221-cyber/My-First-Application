package ru.netology.nmedia.util

import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.entity.PostEntity

fun Post.toEntity(): PostEntity {
    return PostEntity(
        id = this.id,
        author = this.author,
        published = this.published,
        content = this.content,
        likes = this.likes,
        reposts = this.reposts,
        video = this.video,
        likedByMe = this.likedByMe,
        repostedByMe = this.repostedByMe
    )
}