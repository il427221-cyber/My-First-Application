package ru.netology.nmedia.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.netology.nmedia.dto.Post
import java.lang.reflect.Type

class PostRepositoryFileImpl(private val context: Context) : PostRepository {
    private val gson = Gson()
    private var posts = readPosts()
        set(value) {
            field = value
            sync()
        }
    private var nextId = (posts.maxByOrNull { it.id }?.id ?: 0L) + 1L

    private val data = MutableLiveData(posts)

    override fun getAll(): LiveData<List<Post>> = data


    override fun likeById(id: Long) {
        posts = posts.map {
            if (it.id != id) it else it.copy(
                likedByMe = !it.likedByMe,
                likes = if (it.likedByMe) it.likes - 1 else it.likes + 1
            )
        }
        data.value = posts
    }

    override fun repostById(id: Long) {
        posts = posts.map {
            if (it.id != id) it else it.copy(
                repostedByMe = !it.repostedByMe,
                reposts = it.reposts + 1
            )
        }
        data.value = posts
    }

    override fun removeById(id: Long) {
        posts = posts.filter { it.id != id }
        data.value = posts
    }

    override fun save(post: Post) {
        posts = if (post.id == 0L) {
            listOf(
                post.copy(
                    id = nextId++,
                    author = "Me",
                    published = "Now",
                    reposts = post.reposts,
                    video = post.video
                )
            ) + posts
        } else {
            posts.map {
                if (it.id == post.id) {
                    it.copy(content = post.content, reposts = post.reposts, video = post.video)
                } else {
                    it
                }
            }
        }
        data.value = posts
    }

    private fun readPosts(): List<Post> {

        val file = context.filesDir.resolve(FILE_NAME)
        return if (file.exists()) {
            file.reader().buffered().use {
                gson.fromJson(it, postsType)

            }
        } else {
            emptyList()

        }
    }

    private fun sync() {
        val file = context.filesDir.resolve(FILE_NAME)
        file.writer().buffered().use {
            it.write(gson.toJson(posts))
        }
    }

    private companion object {
        const val FILE_NAME = "posts.json"
        val postsType: Type = TypeToken.getParameterized(List::class.java, Post::class.java).type
    }
}