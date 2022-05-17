package ru.netology.nmedia.data.impl

import android.app.Application
import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.netology.nmedia.data.PostRepository
import ru.netology.nmedia.dto.Post

class FilePostRepository(private val application: Application) : PostRepository {

    private val gson = Gson()
    private val type = TypeToken.getParameterized(List::class.java, Post::class.java).type

    override val data: MutableLiveData<List<Post>>

    init {
        val postsFile = application.filesDir.resolve(FILE_NAME)
        val posts: List<Post> = if (postsFile.exists()) {
            val inputStream = application.openFileInput(FILE_NAME)
            val reader = inputStream.bufferedReader()
            reader.use { gson.fromJson(it, type) }
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
            application.openFileOutput(FILE_NAME, Context.MODE_PRIVATE).bufferedWriter().use {
                it.write(gson.toJson(value))
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
        const val FILE_NAME = "posts.json"
    }
}
