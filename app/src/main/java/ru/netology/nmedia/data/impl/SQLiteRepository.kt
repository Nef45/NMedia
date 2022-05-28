package ru.netology.nmedia.data.impl

import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.data.PostRepository
import ru.netology.nmedia.db.PostDao
import ru.netology.nmedia.dto.Post

class SQLiteRepository(
    private val dao: PostDao
) : PostRepository {

    override val data = MutableLiveData(dao.getAll())

    private val listOfPosts
        get() = checkNotNull(data.value) {
            "Data value should not be null"
        }

    override fun like(postId: Long) {
        dao.like(postId)
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
        data.value = newListOfPosts
    }

    override fun share(postId: Long) {
        dao.share(postId)
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
        data.value = newListOfPosts
    }

    override fun delete(postId: Long) {
        dao.delete(postId)
        data.value = listOfPosts.filter { it.id != postId }
    }

    override fun create(post: Post) {
        val newListOfPosts = listOf(dao.save(post)) + listOfPosts
        data.value = newListOfPosts
    }

    override fun update(post: Post) {
        val newListOfPosts = listOfPosts.map { postInList ->
            if (postInList.id == post.id) {
                dao.save(postInList)
            } else {
                postInList
            }
        }
        data.value = newListOfPosts
    }

    fun save(post: Post) {
        if (post.id == PostRepository.NEW_POST_ID) {
            create(post)
        } else {
            update(post)
        }
    }
}