package ru.netology.nmedia.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.PostRepositoryInMemoryImpl
import java.net.URL

private val empty = Post()

class PostViewModel : ViewModel() {

    private val repository: PostRepository = PostRepositoryInMemoryImpl()
    val data = repository.getAll()

    val edited = MutableLiveData(empty)


    fun likeById(id: Long) = repository.likeById(id)
    fun repostById(id: Long) = repository.repostById(id)

    fun removeById(id: Long) = repository.removeById(id)

    fun saveContent(content: String) {
        edited.value?.let { post ->
            val trimmed = content.trim()

            if (post.content != trimmed) {
                repository.save(
                    post.copy(content = trimmed)
                )
            }
            edited.value = empty
        }

    }

    fun edit(post: Post) {
        edited.value = post
    }

}