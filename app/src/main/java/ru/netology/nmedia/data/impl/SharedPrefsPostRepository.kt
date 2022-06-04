package ru.netology.nmedia.data.impl

import android.app.Application
import android.content.Context
import androidx.core.content.edit

import androidx.lifecycle.MutableLiveData
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ru.netology.nmedia.data.PostRepository
import ru.netology.nmedia.dto.Post

class SharedPrefsPostRepository(application: Application) : PostRepository {

    private val prefs = application.getSharedPreferences(
        "repo", Context.MODE_PRIVATE
    )

    override val data: MutableLiveData<List<Post>>

    init {
        val serializedPosts = prefs.getString(POSTS_PREFS_KEY, null)
        val posts: List<Post> = if (serializedPosts != null) {
            Json.decodeFromString(serializedPosts)
        } else {
            emptyList()
        }
        data = MutableLiveData(posts)
    }

    private var listOfPosts
        get() = checkNotNull(data.value) {
            "Data value should not be null"
        }
        set(value) {
            prefs.edit {
                val serializedPosts = Json.encodeToString(value)
                putString(POSTS_PREFS_KEY, serializedPosts)
            }
            data.value = value
        }


    override fun like(postId: Long) {
        val newListOfPosts = listOfPosts.map {
            if (it.id == postId) {
                it.copy(
                    likedByMe = !it.likedByMe,
                    likes = if (!it.likedByMe) {
                        it.likes + 1
                    } else {
                        it.likes - 1
                    }
                )
            } else {
                it
            }
        }
        listOfPosts = newListOfPosts
    }

    override fun share(postId: Long) {
        val newListOfPosts = listOfPosts.map {
            if (it.id == postId) {
                it.copy(
                    shares = it.shares + 1,
                    sharedByMe = true
                )
            } else {
                it
            }
        }
        listOfPosts = newListOfPosts
    }

    override fun delete(postId: Long) {
        listOfPosts = listOfPosts.filter { it.id != postId }
    }

    override fun create(post: Post) {
        listOfPosts = if (listOfPosts.isEmpty()) {
            listOf(
                post.copy(
                    id = 1L
                )
            ) + listOfPosts
        } else {
            listOf(
                post.copy(
                    id = listOfPosts.first().id + 1
                )
            ) + listOfPosts
        }

    }

    override fun update(post: Post) {
        listOfPosts = listOfPosts.map {
            if (it.id == post.id) {
                post
            } else {
                it
            }
        }
    }

    fun save(post: Post) {
        if (post.id == PostRepository.NEW_POST_ID) {
            create(post)
        } else {
            update(post)
        }
    }

    private companion object {
        const val POSTS_PREFS_KEY = "posts"
    }
}
