package ru.netology.nmedia.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.application
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.model.FeedModel
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.PostRepositoryNetworkImpl
import ru.netology.nmedia.util.SingleLiveEvent
import kotlin.concurrent.thread

private val empty = Post()

class PostViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: PostRepository = PostRepositoryNetworkImpl()
    private val _data = MutableLiveData(FeedModel())
    val data: LiveData<FeedModel>
        get() = _data

    private val _posts = MutableLiveData<List<Post>>()
    val posts: LiveData<List<Post>> = _posts
    val edited = MutableLiveData(empty)

    private val _postCreated = SingleLiveEvent<Unit>()

    val postCreated: LiveData<Unit>
        get() = _postCreated

    private val _postUpdated = SingleLiveEvent<Post>()
    val postUpdated: SingleLiveEvent<Post> = _postUpdated

    init {
        loadPosts()
    }

    fun likeById(post: Post) {
        thread{
            val updatedPostFromServer = repository.likeById(post.id, post.likedByMe)

            updatedPostFromServer?.let {newPostData->

                _postUpdated.postValue(newPostData)

                val currentPosts = _posts.value.orEmpty().toMutableList()
                val updatedPostsList = currentPosts.map {
                    if (it.id == newPostData.id) newPostData else it
                }
                _posts.postValue(updatedPostsList)

            }


        }

    }

    fun removeById(id: Long) = repository.removeById(id)

    fun saveContent(content: String) {
        thread {
            try {
                edited.value?.let {
                    val text = content.trim()

                    if (it.content != text) {
                        val result = repository.save(it.copy())
                        println(result)
                        _postCreated.postValue(Unit)
                        clearEditMode()

                    }

                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    }
    fun edit(post: Post) {
        edited.value = post
    }

    fun loadPosts(){
        thread{
            _data.postValue(FeedModel(loading = true))

            _data.postValue(try{
                val posts = repository.getAll()
                FeedModel(posts = posts, empty = posts.isEmpty())
            } catch(e: Exception) {
                FeedModel(error = true)
            }
            )
        }
    }

    fun clearEditMode() {
        edited.postValue(empty)
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