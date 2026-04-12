package ru.netology.nmedia.viewmodel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.application
import ru.netology.nmedia.db.AppDb
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.PostRepositorySQLiteImpl

private val empty = Post()

class PostViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: PostRepository = PostRepositorySQLiteImpl(AppDb.getInstance(application).postDao)
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
            clearEditMode()
        }

    }
    fun edit(post: Post) {
        edited.value = post
    }

    fun clearEditMode() {
        edited.value = empty
    }

    fun saveDraft(content: String) {
        val sharedPrefs = application.getSharedPreferences("draft_prefs", Context.MODE_PRIVATE)
        with(sharedPrefs.edit()) {
            putString("post_draft_content", content)
            apply()
        }
    }

    fun getDraft(): String? {
        val sharedPrefs = application.getSharedPreferences("draft_prefs", Context.MODE_PRIVATE)
        val draft = sharedPrefs.getString("post_draft_content", null)
        return draft

    }

    fun clearDraft() {
        val sharedPrefs = application.getSharedPreferences("draft_prefs", Context.MODE_PRIVATE)
        with(sharedPrefs.edit()) {
            remove("post_draft_content")
            apply()
        }
    }

}