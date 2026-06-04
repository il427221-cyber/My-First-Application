package ru.netology.nmedia.repository


import ru.netology.nmedia.dto.Post

interface PostRepository {
    fun getAll(): List<Post>

    fun likeById(id: Long, likedByMe: Boolean): Post?

    fun removeById(id:Long)

    fun save(post: Post): Post

    fun getAllAsync(callback: GetAllCallback)

    interface GetAllCallback {
        fun onSuccess(posts: List<Post>)
        fun onError(e: Exception)
    }

    fun removePostAsync(id: Long, callback: RemovePostCallback)

    interface RemovePostCallback {
        fun onSuccess()
        fun onError(e: Exception)
    }

    fun savePostAsync(post: Post,callback: SavePostCallback)

    interface SavePostCallback{
        fun onSuccess(post:Post)
        fun onError(e: Exception)
    }

    fun likePostAsync(id: Long, likedByMe: Boolean, callback: LikePostCallback)

    interface LikePostCallback {
        fun onSuccess(post: Post)
        fun onError(e: Exception)
    }

}