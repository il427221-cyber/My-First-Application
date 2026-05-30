package ru.netology.nmedia.repository

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import ru.netology.nmedia.dto.Post
import java.io.IOException
import java.util.concurrent.TimeUnit

class PostRepositoryNetworkImpl : PostRepository {
    private companion object {
        const val BASE_URL = "http://10.0.2.2:9999/api/slow/"
        val jsonType = "application/json".toMediaType()
    }

    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .build()

    private val gson = Gson()
    private val postsType = object : TypeToken<List<Post>>() {}.type

    override fun getAll(): List<Post> {
        val request = Request.Builder()
            .url("${BASE_URL}posts")
            .build()

        val call = client.newCall(request)

        val response = call.execute()

        return gson.fromJson(response.body.string(), postsType)
    }

    override fun likeById(id: Long,likedByMe: Boolean):Post? {
        val likeRequest = Request.Builder()
            .url("${BASE_URL}posts/$id/likes")

        val request: Request = if (likedByMe) {
            likeRequest.delete().build()
        } else {
            likeRequest
                .post(gson.toJson("").toRequestBody(jsonType))
                .build()
        }
        try {
            val response = client.newCall(request).execute()
                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    responseBody?.let {
                        return gson.fromJson(it, Post::class.java)
                    }
                }

        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }

    override fun removeById(id: Long) {
        val request = Request.Builder()
            .url("${BASE_URL}posts/$id")
            .delete()
            .build()

        val call = client.newCall(request)

        call.execute()

    }

    override fun save(post: Post): Post {
        val request = Request.Builder()
            .url("${BASE_URL}posts")
            .post(gson.toJson(post).toRequestBody(jsonType))
            .build()


        val call = client.newCall(request)

        val response = call.execute()

        return gson.fromJson(response.body.string(),Post::class.java)
    }

}