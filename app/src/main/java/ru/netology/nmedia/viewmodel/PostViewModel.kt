package ru.netology.nmedia.viewmodel

import android.annotation.SuppressLint
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

    private val _errorEvent = SingleLiveEvent<String>()
    val errorEvent: LiveData<String>
        get() = _errorEvent


    init {
        loadPosts()
    }

    fun likeById(id: Long) {
        val currentState = _data.value ?: return
        val posts = currentState.posts

        val post = posts.find { it.id == id } ?: return

        repository.likePostAsync(
                post.id,
                post.likedByMe,
                object:PostRepository.LikePostCallback {

                    override fun onSuccess(post: Post?) {
                        val updatedPostsList = posts.map {
                            if (it.id == id) post else it
                        }
                        _data.postValue(currentState.copy(posts = updatedPostsList as List<Post>))

                    }

                    override fun onError(e: Throwable) {
                        _data.value
                        _errorEvent.value = "Не удалось проставить/снять лайк посту с ID $id: ${e.message}"
                    }

                })
    }


    @SuppressLint("SuspiciousIndentation")
    fun removeById(id: Long) {
            val currentPosts = _data.value?.posts.orEmpty()

                repository.removePostAsync(id, object:PostRepository.RemovePostCallback {
                    override fun onSuccess() {
                        _data.postValue(
                            _data.value?.copy(posts = currentPosts
                                .filter{it.id != id}
                            )
                        )
                    }
                    override fun onError(e: Throwable) {
                        _data.postValue(_data.value?.copy(posts = currentPosts))
                        _errorEvent.value = "Не удалось удалить пост с ID $id: ${e.message}"
                    }
                })
    }

    fun saveContent(content: String) {
                edited.value?.let {
                    val text = content.trim()

                    if (it.content != text) {
                        repository.savePostAsync(
                            it.copy(content = text),
                            object: PostRepository.SavePostCallback {
                                override fun onSuccess(post: Post) {
                                    _postCreated.postValue(Unit)
                                    clearEditMode()
                                }

                                override fun onError(e: Throwable) {
                                    e.printStackTrace()
                                    _errorEvent.value = "Не удалось сохранить пост: ${e.message}"
                                }

                            })
                    }

                }
    }

    fun edit(post: Post) {
            repository.editPostAsync(
                post,
                object : PostRepository.EditPostCallback {
                    override fun onSuccess(post: Post?) {
                        edited.value = post
                    }

                    override fun onError(e: Throwable) {
                        _errorEvent.value = "Не удалось отредактировать пост: ${e.message}"
                    }

                })

        }


    fun loadPosts(){
            _data.postValue(FeedModel(loading = true))
            repository.getAllAsync(object: PostRepository.GetAllCallback{
            override fun onSuccess(posts: List<Post>) {
                _data.value = FeedModel(posts = posts, empty = posts.isEmpty())
            }

            override fun onError(e: Throwable) {
                _data.value = FeedModel(error = true)
            }

        })
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