package ru.netology.nmedia.repository


import ru.netology.nmedia.dto.Post

interface PostRepository {
    fun getAll(): List<Post>

    fun removeById(id:Long)

    fun save(post: Post)

    fun getAllAsync(callback: GetAllCallback)

    interface GetAllCallback {
        fun onSuccess(posts: List<Post>)
        fun onError(e: Throwable)
    }

    fun removePostAsync(id: Long, callback: RemovePostCallback)

    interface RemovePostCallback {
        fun onSuccess()
        fun onError(e: Throwable)
    }

    fun savePostAsync(post: Post,callback: SavePostCallback)

    interface SavePostCallback{
        fun onSuccess(post:Post)
        fun onError(e: Throwable)
    }

    fun likePostAsync(id: Long, likedByMe: Boolean, callback: LikePostCallback)

    interface LikePostCallback {
        fun onSuccess(post: Post?)
        fun onError(e: Throwable)
    }

    fun editPostAsync(post: Post, callback: EditPostCallback)

    interface EditPostCallback {
        fun onSuccess(post: Post?)
        fun onError(e: Throwable)
    }





}