package ru.netology.nmedia.repository

import android.annotation.SuppressLint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.netology.nmedia.api.PostApi
import ru.netology.nmedia.dto.Post


class PostRepositoryNetworkImpl : PostRepository {
    override fun getAll(): List<Post> {
        return PostApi.service.getAll()
            .execute()
            .body()
            .orEmpty()
    }

    override fun getAllAsync(callback: PostRepository.GetAllCallback) {

        PostApi.service.getAll()
            .enqueue(object : Callback<List<Post>> {

                override fun onResponse(call: Call<List<Post>>, response: Response<List<Post>>) {
                    if (response.isSuccessful) {
                        val body = response.body() ?: run {
                            callback.onError(RuntimeException("Body is empty"))
                            return
                        }
                        callback.onSuccess(body)
                    }
                }

                override fun onFailure(call: Call<List<Post>>, t: Throwable) {
                    callback.onError(e = t)
                }

            })
    }

    override fun save(post: Post) {
        PostApi.service.save(post)
            .execute()
    }

    @SuppressLint("SuspiciousIndentation")
    override fun savePostAsync(post: Post, callback: PostRepository.SavePostCallback) {

        PostApi.service.save(post)
            .enqueue(object : Callback<Post> {
                override fun onResponse(call: Call<Post>, response: Response<Post>) {
                    if (response.isSuccessful) {
                        val body = response.body() ?: run {
                            callback.onError(RuntimeException("Body is empty"))
                            return
                        }
                        callback.onSuccess(body)
                    }
                }

                override fun onFailure(call: Call<Post>, t: Throwable) {
                    callback.onError(t)
                }

            })

    }

    override fun removeById(id: Long) {
        PostApi.service.deleteById(id)
            .execute()
    }


    override fun removePostAsync(id: Long, callback: PostRepository.RemovePostCallback) {
        PostApi.service.deleteById(id)
            .enqueue(object : Callback<Unit> {
                override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                    callback.onSuccess()
                }

                override fun onFailure(call: Call<Unit>, t: Throwable) {
                    callback.onError(t)
                }
            })
    }
    override fun likePostAsync(
        id: Long,
        likedByMe: Boolean,
        callback: PostRepository.LikePostCallback
    ) {

        if (likedByMe) {
            PostApi.service.dislikeById(id)
                .enqueue(object : Callback<Post> {
                    override fun onResponse(call: Call<Post>, response: Response<Post>) {
                        if (response.isSuccessful) {
                            val body = response.body()
                            if (body != null) {
                                callback.onSuccess(body)
                            } else {
                                callback.onError(RuntimeException("Response body is null for dislike, code: ${response.code()}"))
                            }
                        } else {
                            callback.onError(RuntimeException("HTTP error for dislike: ${response.code()} ${response.message()}"))
                        }
                    }

                    override fun onFailure(call: Call<Post>, t: Throwable) {
                        callback.onError(t)
                    }
                })
        } else {
            PostApi.service.likeById(id)
                .enqueue(object : Callback<Post> {
                    override fun onResponse(call: Call<Post>, response: Response<Post>) {
                        if (response.isSuccessful) {
                            val body = response.body()
                            if (body != null) {
                                callback.onSuccess(body)
                            } else {
                                callback.onError(RuntimeException("Response body is null for like, code: ${response.code()}"))
                            }
                        } else {
                            callback.onError(RuntimeException("HTTP error for like: ${response.code()} ${response.message()}"))
                        }
                    }

                    override fun onFailure(call: Call<Post>, t: Throwable) {
                        callback.onError(t)
                    }
                })

        }

    }


    override fun editPostAsync(post: Post, callback: PostRepository.EditPostCallback) {
        PostApi.service.edit(post)
            .enqueue(object : Callback<Post> {
                override fun onResponse(call: Call<Post>, response: Response<Post>) {
                    if (response.isSuccessful) {
                        val body = response.body() ?: run {
                            callback.onError(RuntimeException("Body is empty"))
                            return
                        }
                        callback.onSuccess(body)
                    }
                }

                override fun onFailure(call: Call<Post>, t: Throwable) {
                    callback.onError(t)
                }

            })
    }
}